package com.myself.MySweety.core.recognize;


import com.myself.MySweety.config.FieldConfigProperties;

import javax.sound.sampled.*;
import java.io.File;

//进行录音
public class VoiceRecorder {
    private static FieldConfigProperties config = new FieldConfigProperties();
    private static TargetDataLine targetDataLine;  //录入
    private static AudioFormat audioFormat;

    public void menu() {
        System.out.println("正在聆听...");
        newRecorder();
    }

    private void newRecorder() {
        /*
         *设置音频的格式
         */
        //采样率，从8000,11025,16000,22050,44100
        float sampleRate = 8000F;
        //每个样本的中位数 8,16
        int sampleSizeInBits = 16;
        //单声道为1，立体声为2
        int channels = 2;
        //singned
        boolean signed = true;
        //以大端还是小端的顺序来存储音频数据
        boolean bigEndian = true;
        audioFormat = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);

        //构造数据行的信息对象，这个信息包括单个音频格式
        //lineClass-该信息对象所描述的数据行的类
        //format：所需的格式
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            new RecorderThread().start();
            Thread.sleep(10000); //等待10秒后结束录音
            endRecorder();
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    class RecorderThread extends Thread {
        @Override
        public void run() {
            //指定的文件类型     wav格式文件
            AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
            //设置文件类型和文件扩展名
            File audioFile = new File(config.getRecordFile());
            try {
                targetDataLine.open(audioFormat);
                //开始音频捕获，回放，生成start事件
                targetDataLine.start();
                AudioInputStream in = new AudioInputStream(targetDataLine);
                AudioSystem.write(in, fileType, audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //结束录音
    private void endRecorder() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}
