package com.myself.MySweety.config;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//属性配置文件（个人信息类）
@Data
public class FieldConfigProperties {

    //语音识别时的录音文件名
    private String recordFile;

    //创建机器人应用后的信息
    private String jarvisApiKey;
    private String jarvisUserId;
    private String jarvisUrl;
    private String jarvisDevPid;

    //创建百度语音应用后的信息
    private String bdAppId;
    private String bdApiKey;
    private String bdSecretKey;

    //语音合成时的录音文件名
    private String synthesisFile;

    public FieldConfigProperties() {
        //加载配置文件
        InputStream inputStream = FieldConfigProperties.class.getClassLoader().getResourceAsStream("fieldConfig.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.recordFile=String.valueOf(p.getProperty("record_file"));

        this.jarvisApiKey = String.valueOf(p.getProperty("json.api_key"));
        this.jarvisUserId = String.valueOf(p.getProperty("json.user_id"));
        this.jarvisUrl = String.valueOf(p.getProperty("json.url"));
        this.jarvisDevPid=String.valueOf(p.getProperty("json.dev_pid"));

        this.bdAppId = String.valueOf(p.getProperty("bd.app_id"));
        this.bdApiKey = String.valueOf(p.getProperty("bd.api_key"));
        this.bdSecretKey = String.valueOf(p.getProperty("bd.secret_key"));

        this.synthesisFile=String.valueOf(p.getProperty("synthesis_file"));
    }
}
