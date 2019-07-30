package com.myself.MySweety.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

//命令配置文件
public class OrderConfigProperties {
    public String getValues(String key) {
        InputStream in = OrderConfigProperties.class.getClassLoader().getResourceAsStream("orderConfig.properties");
        Properties p = new Properties();
        try {
            p.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p.getProperty(key);
    }
}
