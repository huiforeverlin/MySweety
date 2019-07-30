package com.myself.MySweety.core.synthesis;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.myself.MySweety.config.FieldConfigProperties;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

//语音合成：文本转语音
public class TextConvertSpeech {

    private FieldConfigProperties config = new FieldConfigProperties();
    private final AipSpeech client;

    private VoicePlayer voicePlayer;

    public TextConvertSpeech(VoicePlayer voicePlayer, AipSpeech client) {
        this.voicePlayer = voicePlayer;
        this.client = client;
    }

    public void doSyn(String text) {
        if (getMp3(text)) {
            playMp3();
        } else {
            System.out.println("转换失败");
        }
    }

    //将文本转换为MP3文件
    private boolean getMp3(String text) {
        // 设置可选参数
        HashMap<String, Object> options = new HashMap<>();
        //语速，取值0-9，默认为5中语速
        options.put("spd", "5");
        //音调，取值0-9，默认为5中语调
        options.put("pit", "5");
        //发音人选择，0为女生，1为男生，3为度逍遥，4为度丫丫
        options.put("per", "0");
        //音量0-15,5为中音量
        options.put("vol", 5);

        //调用接口，进行语音合成
        TtsResponse res = client.synthesis(text, "zh", 1, options);
        JSONObject res1 = res.getResult(); //服务器返回的内容，合成成功时为null,失败时包含error_no等信息
        //生成的音频数据
        byte[] data = res.getData();
        if (data != null) {
            try {
                //写入到文件中去
                Util.writeBytesToFileSystem(data, config.getSynthesisFile());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        // 失败返回
        /*{
                "err_no":500,
                "err_msg":"notsupport.",
                "sn":"abcdefgh",
                "idx":1
        }
        */
        //indentFactor缩进因子
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }
        return true;
    }

    //播放MP3文件
    private void playMp3() {
        voicePlayer = new VoicePlayer(config.getSynthesisFile());
        voicePlayer.play();
    }

}
