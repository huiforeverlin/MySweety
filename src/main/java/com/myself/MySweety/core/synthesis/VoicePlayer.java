package com.myself.MySweety.core.synthesis;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// 播放语音
public class VoicePlayer {
    private Player player;
    private String voiceFile;

    public VoicePlayer(String voiceFile) {
        this.voiceFile = voiceFile;
    }

    public void play() {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(voiceFile));
            player = new Player(in);
            player.play();
        } catch (JavaLayerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
