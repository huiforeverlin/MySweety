package com.myself.MySweety.core.json;


import com.myself.MySweety.config.FieldConfigProperties;
import com.myself.MySweety.core.json.toJson.InputText;
import com.myself.MySweety.core.json.toJson.Perception;
import com.myself.MySweety.core.json.toJson.UserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//进行字符串的序列化（普通字符串 -> Json串）和反序列化（Json串 -> 普通字符串）
public class JsonTest {
    private static FieldConfigProperties config = new FieldConfigProperties();
    private static final String apiKey = config.getJarvisApiKey();
    private static final String userId = config.getJarvisUserId();
    private static final String url = config.getJarvisUrl();

    //总的调用方法
    public  String json(String str) {
        //1. 构造Json串【序列化】
        String res1 = buildJson(str);
        //2. 发起请求
        String res2 = sendPost(res1);
        //3. 解析Json串【反序列化】
        return parseJson(res2);
    }

    //向机器人发起请求
    private  String sendPost(String str) {
        StringBuilder responseStr = new StringBuilder();
        BufferedReader in = null;
        try {
            URL Url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            connection.connect();

            //将字节流转换成字符流  往出写
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(str);
            out.close();
            connection.getOutputStream().flush();
            //读取响应结果  BufferedReader 字符缓冲输入流
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                responseStr.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭输入流
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseStr.toString();
    }

    //序列化：构建Json串
    private  String buildJson(String str) {
        JSONObject jsonObject = new JSONObject();

        UserInfo userInfo = new UserInfo();
        userInfo.setApiKey(apiKey);
        userInfo.setUserId(userId);

        InputText inputText = new InputText();
        inputText.setText(str);

        Perception perception = new Perception();
        perception.setInputText(inputText);

        int reqType = 0;

        jsonObject.put("reqType", reqType);
        jsonObject.put("perception", perception);
        jsonObject.put("userInfo", userInfo);

        return jsonObject.toString();
    }

    //解析Json串
    private  String parseJson(String s) {
        JSONObject jb = JSONObject.fromObject(s);
        JSONArray js = jb.getJSONArray("results");
        JSONObject jbS = JSONObject.fromObject(js.getJSONObject(0).toString());
        String tmp = jbS.get("resultType").toString();
        if (tmp.equals("text")) {
            return (String) JSONObject.fromObject(jbS.get("values")).get("text");
        }
        if (tmp.equals("url")) {
            return (String) JSONObject.fromObject(jbS.get("values")).get("url");
        }
        return null;
    }
}


