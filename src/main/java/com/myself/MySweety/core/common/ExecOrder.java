package com.myself.MySweety.core.common;

import java.io.IOException;

//执行命令功能的类
public class ExecOrder {
    public  void execOrder(String order){
        Runtime runtime=Runtime.getRuntime();
        try {
            runtime.exec(order);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

