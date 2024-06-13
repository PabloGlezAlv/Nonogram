package com.example.game;
import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Input;

import java.util.List;

public interface Scene {

    public void init(Engine e, Game g);
    public void update(float deltaTime);
    public void render(Graphics g);
    public void handleInput(List<Input.TouchEvent> events);
}
