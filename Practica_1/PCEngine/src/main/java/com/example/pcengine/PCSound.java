package com.example.pcengine;

import com.example.engine.Audio;
import com.example.engine.Sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class PCSound implements Sound {

    public PCSound(String file) {
        try {
            File audio = new File("./data/assets/"+file + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio);
            soundClip = AudioSystem.getClip();
            soundClip.open(audioStream);
        } catch(Exception e) {
            System.out.println("Fallo al cargar el audio: " + e);
        }

        name = file;
        if(name == "background_musi") soundClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void play() {
        soundClip.setFramePosition(0);
        soundClip.start();
    }

    @Override
    public boolean isPlaying() {
        return soundClip.isActive();
    }

    @Override
    public void stop() {
        soundClip.stop();
    }

    public Clip getClip() {
        return soundClip;
    }

    Clip soundClip;
    String name;

}
