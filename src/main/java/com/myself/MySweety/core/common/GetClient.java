package com.myself.MySweety.core.common;

import com.baidu.aip.speech.AipSpeech;
import com.myself.MySweety.config.FieldConfigProperties;

//获取百度客户端  单例
public class GetClient {
    private FieldConfigProperties config = new FieldConfigProperties();
    private final String APP_ID = config.getBdAppId();
    private final String API_KEY = config.getBdApiKey();
    private final String SECRET_KEY = config.getBdSecretKey();

    private static volatile GetClient client;

    private GetClient() {

    }

    public static GetClient getInstance() {
        if (client == null) {
            synchronized (GetClient.class) {
                if (client == null) {
                    client = new GetClient();
                }
            }
        }
        return client;
    }

    public AipSpeech getAipSpeech() {
        return new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
    }
}
