package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Image;
import com.example.engine.Input;

import java.util.List;

public class WorldsScene implements Scene {

    Button volverButton;
    Button leftTop;
    Button leftButtom;
    Button rightTop;
    Button rightbuttom;

    Image volverButtonImage;

    Image leftTopLevels;
    Image rightTopLevels;
    Image leftButtomLevels;
    Image rightButtomLevels;

    Engine e;

    Game g;

    @Override
    public void init(Engine e_, Game g_) {

        e = e_;
        g = g_;

        gW = g.getGameWidth();
        gH = g.getGameHeight();

        worldsImageW = gW*4/10;
        worldsImageH = gW*4/10;
        worldsImageX = gW/2-worldsImageW;
        worldsImageY = gH/2-worldsImageH;

        leftTop = new Button(worldsImageX, worldsImageY, worldsImageW, worldsImageH, "a", true, g);
        leftButtom = new Button(worldsImageX, worldsImageY+ gW/10 + worldsImageH, worldsImageW, worldsImageH, "ag", true, g);
        rightTop = new Button(worldsImageX + worldsImageW, worldsImageY, worldsImageW, worldsImageH, "f", true, g);
        rightbuttom = new Button(worldsImageX + worldsImageW, worldsImageY+ gW/10 + worldsImageH, worldsImageW, worldsImageH, "t", true, g);

        volverButton = new Button(20, 20, gW/10, gW/10, "back", true, g);
        volverButtonImage =  e.getGraphics().newImage("backButton.png");

        leftTopLevels = e.getGraphics().newImage("a.png");
        rightTopLevels = e.getGraphics().newImage("f.png");
        leftButtomLevels = e.getGraphics().newImage("ag.png");
        rightButtomLevels = e.getGraphics().newImage("t.png");
    }

    @Override
    public void update(float deltaTime) {
        //
    }

    @Override
    public void render(Graphics g) {
        g.setColor(PlayerState.wordColor);

        g.drawImage(volverButtonImage, 20, 20, gW/10, gW/10);

        g.drawImage(leftTopLevels, worldsImageX, worldsImageY, worldsImageW, worldsImageH);
        g.drawImage(rightTopLevels, worldsImageX + worldsImageW, worldsImageY, worldsImageW, worldsImageH);
        g.drawImage(leftButtomLevels, worldsImageX, worldsImageY+ gW/10 + worldsImageH, worldsImageW, worldsImageH);
        g.drawImage(rightButtomLevels, worldsImageX + worldsImageW, worldsImageY+ gW/10 + worldsImageH, worldsImageW, worldsImageH);

    }

    @Override
    public void handleInput(List<Input.TouchEvent> events) {
        if (!events.isEmpty()) {
            for (Input.TouchEvent ev : events) {
                volverButton.handleInput(ev);
                leftTop.handleInput(ev);
                leftButtom.handleInput(ev);
                rightTop.handleInput(ev);
                rightbuttom.handleInput(ev);
            }
            events.clear();
        }
    }

    // Lista de funciones para los botones
    public void buttonFunction(String function_) {
        switch (function_) {
            case "back":
                e.getAudio().playSound("click_sound");
                g.toInitScene();
                break;
            case "a":
            case "ag":
            case "f":
            case "t":
                e.getAudio().playSound("click_sound");
                g.toChooseLevel(function_);
                break;
        }
    }

    int worldsImageW = 0;
    int worldsImageH = 0;
    int worldsImageX = 0;
    int worldsImageY = 0;

    int gW = 0;
    int gH = 0;
}
