package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Image;
import com.example.engine.Input;

import java.util.List;

public class MenuScene implements Scene{

    Button levelRandom;
    Button level5;
    Button level8;
    Button level10;
    Button volverButton;

    Font titleFont;
    Font levelFont1;
    Font levelFont2;

    Image volverButtonImage;

    int fontSize;

    Engine e;

    Game g;

    @Override
    public void init(Engine e_, Game g_) {
        e = e_;
        g= g_;

        gW = g.getGameWidth();
        gH = g.getGameHeight();
        buttonwidth = gW/2;
        buttonheight = gH/10;
        buttonposx = gW / 2 - buttonwidth/2;
        buttonposy = gH*4/10;

        level5 = new Button(buttonposx, buttonposy, buttonwidth, buttonheight, "5x5", false, g);
        level8 = new Button(buttonposx, buttonposy + buttonheight + 10, buttonwidth, buttonheight, "8x8", false, g);
        level10 = new Button(buttonposx, buttonposy + (buttonheight+10) * 2, buttonwidth, buttonheight, "10x10", false, g);
        levelRandom = new Button(buttonposx, buttonposy + (buttonheight+10) * 3, buttonwidth, buttonheight, "random", false, g);

        volverButton = new Button(20, 20, gW/10, gW/10, "back", true, g);
        volverButtonImage =  e.getGraphics().newImage("backButton.png");

        fontSize = gW/7;
        if(fontSize>130) fontSize = 130;

        titleFont = e.getGraphics().newFont("JosefinSans-Bold.ttf", fontSize, false);
        levelFont1 = e.getGraphics().newFont("JosefinSans-Bold.ttf", fontSize, false);
        levelFont2 = e.getGraphics().newFont("JosefinSans-Bold.ttf", fontSize*2/3, false);
    }

    @Override
    public void update(float deltaTime) {
        //
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(volverButtonImage, 20, 20, gW/10, gW/10);

        g.setColor(PlayerState.wordColor);
        g.setFont(titleFont);
        g.drawCenteredText("Nonogramas", 0, 0, gW, gH/2);
        g.setFont(levelFont1);
        g.drawCenteredText("5x5", level5.getX_(), level5.getY_(), buttonwidth, buttonheight);
        g.drawCenteredText("8x8", level8.getX_(), level8.getY_(), buttonwidth, buttonheight);
        g.drawCenteredText("10x10", level10.getX_(), level10.getY_(), buttonwidth, buttonheight);
        g.setFont(levelFont2);
        g.drawCenteredText("RANDOM", levelRandom.getX_(), levelRandom.getY_(), buttonwidth, buttonheight);
    }

    @Override
    public void handleInput(List<Input.TouchEvent> events) {
        if(!events.isEmpty()) {
            for (Input.TouchEvent ev : events) {
                level5.handleInput(ev);
                level8.handleInput(ev);
                level10.handleInput(ev);
                levelRandom.handleInput(ev);
                volverButton.handleInput(ev);
            }
            events.clear();
        }
    }

    // Lista de funciones para los botones
    public void buttonFunction(String function_) {
        switch (function_) {
            case "5x5":
                e.getAudio().playSound("click_sound");
                g.toGameScene("5x5/1", false);
                break;
            case "8x8":
                e.getAudio().playSound("click_sound");
                g.toGameScene("8x8/1", false);
                break;
            case "10x10":
                e.getAudio().playSound("click_sound");
                g.toGameScene("10x10/1", false);
                break;
            case "random":
                e.getAudio().playSound("click_sound");
                g.toGameScene("random", false);
                break;
            case "back":
                e.getAudio().playSound("click_sound");
                g.toInitScene();
                break;
        }
    }

    int buttonwidth = 0;
    int buttonheight = 0;
    int buttonposx = 0;
    int buttonposy = 0;

    //Tamaños de la pantalla de juego
    int gW = 0;
    int gH = 0;
}
