package com.myself.MySweety.core.recognize;

import com.baidu.aip.speech.AipSpeech;
import com.myself.MySweety.config.FieldConfigProperties;
import com.myself.MySweety.core.common.GetClient;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

//语音识别：语音转文本
public class SpeechRec {

    private FieldConfigProperties config = new FieldConfigProperties();
    private final AipSpeech aipSpeech;
    // 设置本地语音文件路径
    // 获取当前工程路径：System.getProperty("user.dir")
    private String recordPath = System.getProperty("user.dir") + File.separator + config.getRecordFile();

    public SpeechRec(AipSpeech aipSpeech) {
        this.aipSpeech = aipSpeech;
    }

    public String getResult() {
        String path = recordPath;
        if (Paths.get(path).toFile().exists()) {  //如果本地语音文件存在，则向远程服务上传整段语音进行识别
            HashMap<String, Object> options = new HashMap<>();
            options.put("dev_pid", config.getJarvisDevPid()); //设置语音类别
            // 对本地语音文件进行识别
            org.json.JSONObject asrRes = aipSpeech.asr(path, "wav", 16000, options);
            // 成功返回
            /*{
                    "err_no": 0,
                    "err_msg": "success.",
                    "corpus_no": "15984125203285346378",
                    "sn": "481D633F-73BA-726F-49EF-8659ACCC2F3D",
                    "result": ["北京天气"]
            }
            */

            // 失败返回
            /*{
                    "err_no": 2000,
                    "err_msg": "data empty.",
                    "sn": null
            }
            */

            // 提取返回的结果信息
            if (asrRes.getString("err_msg").equals("success.")) {//表示成功返回
                return asrRes.getJSONArray("result").getString(0);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        while (true){
            VoiceRecorder voiceRecorder=new VoiceRecorder();
            voiceRecorder.menu();
            SpeechRec speechRec=new SpeechRec(GetClient.getInstance().getAipSpeech());
            System.out.println("正在识别");
            System.out.println(speechRec.getResult());
        }
    }
}
