package com.myself.MySweety.core;

import com.myself.MySweety.config.FieldConfigProperties;
import com.myself.MySweety.config.OrderConfigProperties;
import com.myself.MySweety.core.common.ExecOrder;
import com.myself.MySweety.core.common.GetClient;
import com.myself.MySweety.core.common.OpenFile;
import com.myself.MySweety.core.json.JsonTest;
import com.myself.MySweety.core.recognize.SpeechRec;
import com.myself.MySweety.core.recognize.VoiceRecorder;
import com.myself.MySweety.core.synthesis.TextConvertSpeech;
import com.myself.MySweety.core.synthesis.VoicePlayer;
import com.myself.everythingP.core.EverythingPManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MySweetyManager {
    private FieldConfigProperties config = new FieldConfigProperties();
    private OrderConfigProperties configProperties = new OrderConfigProperties();
    private GetClient client = GetClient.getInstance();
    private VoicePlayer voicePlayer = new VoicePlayer(config.getSynthesisFile());
    private TextConvertSpeech textConvertSpeech = new TextConvertSpeech(voicePlayer, client.getAipSpeech());
    private ExecOrder execOrder = new ExecOrder();


    public MySweetyManager() {

    }

    public void start() {
        new Thread(this::prepareJob).start();
        textConvertSpeech.doSyn("主人有什么需求尽管吩咐(:");
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 1. 录音
            initVoiceRecorder();
            System.out.println("正在识别...");
            // 2. 语音识别
            String res1 = initSpeechRec();
            if (res1 != null) {
                System.out.println("我# " + res1);
                // 3. 判断
                // 3.1 判断是否要退出
                if ("退出。".equals(res1)) {
                    textConvertSpeech.doSyn("欢迎使用，主人下次再见");
                    break;
                }
                // 3.2 判断是否要查询文件
                if (res1.startsWith("查询文件") && res1.contains("文件名为")) {
                    int index = res1.indexOf("为");
                    String fileName = res1.substring(index + 1,res1.length()-1); //去掉最后那个标点符号
                    openFile(fileName);
                    continue;
                }
                // 3.3 判断是否是命令
                String order = configProperties.getValues(res1);
                // 3.3.1 是命令
                if (order != null) {
                    textConvertSpeech.doSyn("好的主人(:");
                    execOrder.execOrder(order);
                } else { //3.3.2 不是命令
                    //  3.3.2.1 向机器人发起请求
                    String res2 = initJsonTest(res1);
                    // 判断是否是网址 如果是，直接打开对应网页
                    if (res2.startsWith("http")) {
                        openUrl(res2);
                    } else {
                        // 3.3.2.2 语音合成，播放结果
                        System.out.println("正在合成...");
                        printOneByOne(res2);
                        initSynPlay(res2);
                    }
                }
            } else {
                textConvertSpeech.doSyn("抱歉主人，我没有听清楚<(_ _)>");
            }
        }
    }

    //开始录音、结束录音
    public void initVoiceRecorder() {
        VoiceRecorder voiceRecorder = new VoiceRecorder();
        voiceRecorder.menu();
    }

    //语音识别
    public String initSpeechRec() {
        SpeechRec speechRec = new SpeechRec(client.getAipSpeech());
        return speechRec.getResult();
    }

    //Json串的序列化和反序列化
    private String initJsonTest(String res1) {
        JsonTest jsonTest = new JsonTest();
        return jsonTest.json(res1);
    }

    //语音合成
    private void initSynPlay(String res2) {
        textConvertSpeech.doSyn(res2);
    }

    //逐字打印
    private void printOneByOne(String words) {
        System.out.print("机器人# ");
        char[] chars = words.toCharArray();
        for (char aChar : chars) {
            System.out.print(aChar);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    //打开网页
    private void openUrl(String url) {
        try {
            URI uriNet = new URI(url);
            Desktop.getDesktop().browse(uriNet);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    //打开文件
    private void openFile(String fileName) {
        OpenFile.openFile(fileName);
    }

    //开启清理线程和文件监控
    private void prepareJob() {
        EverythingPManager.getInstance().startFileSystemMonitor();
        EverythingPManager.getInstance().startThingClearThread();
    }
}
