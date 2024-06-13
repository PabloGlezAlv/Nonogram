package com.example.engine;

public interface Audio {

    public Sound newSound(String file, Boolean music);

    Sound playSound(String id);

    void stopSound(String id);
}
