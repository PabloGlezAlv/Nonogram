package com.example.pcengine;

import com.example.engine.Audio;
import com.example.engine.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PCAudio implements Audio {

    public PCAudio() {
        soundMap = new HashMap<String, PCSound>();
    }

    @Override
    public Sound newSound(String file, Boolean music) {
        PCSound ret = new PCSound(file);
        soundMap.put(file, ret);
        return ret;
    }

    @Override
    public Sound playSound(String id) {
        PCSound sound = soundMap.get(id);
        sound.play();
        return sound;
    }

    @Override
    public void stopSound(String id) {
        PCSound sound = soundMap.get(id);
        sound.stop();
    }

    private HashMap<String, PCSound> soundMap;
}
