package com.example.androidengine;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;

import com.example.engine.Sound;

import java.io.IOException;
import java.net.URI;

public class AndroidSound implements Sound {

    public AndroidSound(String file, AssetManager a, Boolean music) throws IOException {

        longSound = music;
        aManager = a;
        if(!music)
        {

            spool = new SoundPool.Builder().setMaxStreams(5).build();

            try {
                AssetFileDescriptor afd = aManager.openFd("data/assets/"+file + ".ogg");
                soundId = spool.load(afd, 1);

            } catch(Exception e) {
                throw new RuntimeException("Couldn`t open file: " + e);
            }
        }
        else
        {
            mp = new MediaPlayer();
            mp.reset();
            try {
                AssetFileDescriptor afd = aManager.openFd("data/assets/"+file + ".ogg");
                mp.setDataSource(afd.getFileDescriptor(),
                        afd.getStartOffset(), afd.getLength());
                mp.prepare();
                mp.setLooping(true);
            } catch (IOException e) {
                System.err.println("Couldn't load audio file");
                e.printStackTrace();
            }

        }

        name = file;
    }

    @Override
    public void play() {
        if(!longSound)
        {
            spool.play(soundId, 1, 1, 1, 0, 1);
        }
        else
        {
            mp.start();
        }
    }

    @Override
    public boolean isPlaying() {
        return true;
    }

    @Override
    public void stop() {
        if(!longSound)
        {
            spool.stop(soundId);
        }
        else
        {
            mp.stop();
        }
    }

    SoundPool spool;

    AssetManager aManager;

    MediaPlayer mp;

    int soundId = -1;

    String name;

    boolean longSound;
}
