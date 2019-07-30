package com.myself.MySweety.core.common;


import com.myself.MySweety.config.FieldConfigProperties;
import com.myself.MySweety.core.MySweetyManager;
import com.myself.MySweety.core.synthesis.TextConvertSpeech;
import com.myself.MySweety.core.synthesis.VoicePlayer;
import com.myself.everythingP.cmd.everythingCmdApp;
import com.myself.everythingP.core.EverythingPManager;
import com.myself.everythingP.core.model.Condition;
import com.myself.everythingP.core.model.Thing;

import java.util.List;

//执行打开文件功能的类
public class OpenFile {
    private static FieldConfigProperties config = new FieldConfigProperties();
    private static GetClient client = GetClient.getInstance();
    private static VoicePlayer voicePlayer = new VoicePlayer(config.getSynthesisFile());
    private static TextConvertSpeech textConvertSpeech = new TextConvertSpeech(voicePlayer, client.getAipSpeech());

    public static void openFile(String fileName) {
        MySweetyManager mySweetyManager = new MySweetyManager();
        Condition condition = new Condition();
        condition.setName(fileName);
        EverythingPManager manager = EverythingPManager.getInstance();
        List<Thing> things = everythingCmdApp.search(manager, condition);
        if (things.size() != 0) {
            textConvertSpeech.doSyn("请主人选择一个想打开的文件");
            mySweetyManager.initVoiceRecorder();
            System.out.println("正在识别...");
            String one = mySweetyManager.initSpeechRec();
            System.out.println("我# " + one);
            String chooseOne;
            StringBuilder sb = new StringBuilder();
            if (one.length() != 0 && (one.startsWith("第")||one.startsWith("D")) && one.endsWith("个文件。")) {
                int endIndex = one.indexOf("个");
                chooseOne = one.substring(1, endIndex);
                if (chooseOne.length() == 1 && chooseOne.charAt(0) == '十') {
                    sb.append("10");
                } else {
                    for (char c : chooseOne.toCharArray()) {
                        if (c >= '0' && c <= '9') {  //如果返回结果就是数字，就直接append
                            sb.append(c);
                        } else { //汉字转数字后append
                            sb.append(ZiFun.convertToNum(c));
                        }
                    }
                }
            }
            if (sb.length() != 0) {
                chooseOne = sb.toString();
                int index = Integer.parseInt(chooseOne);
                try {
                    String path = things.get(index - 1).getPath();
                    textConvertSpeech.doSyn("好的主人");
                    String order = "rundll32 url.dll FileProtocolHandler file://" + path; //打开文件的执行命令
                    ExecOrder order1 = new ExecOrder();
                    order1.execOrder(order);
                } catch (RuntimeException e) { //要取的文件下标不能超过集合的个数
                    System.out.println("主人，您选的文件所在下标超过此次文件总数啦！");
                }
            } else {
                textConvertSpeech.doSyn("打开失败，请主人重新选择");
            }
        } else {
            textConvertSpeech.doSyn("抱歉主人，没有找到相关文件，请重新选择或等待片刻");
        }
    }
}
