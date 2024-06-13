package com.example.androidengine;

import android.content.Context;

import com.example.engine.Sound;

import java.io.IOException;
import java.util.HashMap;

public class AndroidAudio implements com.example.engine.Audio{

    public AndroidAudio() {
        soundMap = new HashMap<String, AndroidSound>();


    }

    @Override
    public Sound newSound(String file, Boolean music) {
        AndroidSound ret;
        try {
            ret = new AndroidSound(file, context.getAssets(), music);
            soundMap.put(file, ret);
            return ret;
        } catch (IOException e) {
            System.out.print(e.getStackTrace());
            return null;
        }
    }

    @Override
    public Sound playSound(String id) {
        AndroidSound sound = soundMap.get(id);
        sound.play();
        return sound;
    }

    @Override
    public void stopSound(String id) {
        AndroidSound sound = soundMap.get(id);
        sound.stop();
    }

    public void setContext(Context c_) { context = c_; }

    Context context;
    private HashMap<String, AndroidSound> soundMap;

}
