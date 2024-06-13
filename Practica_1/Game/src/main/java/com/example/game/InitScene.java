package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Image;
import com.example.engine.Input;

import java.awt.Rectangle;
import java.util.List;

public class InitScene implements Scene {

    Button play;
    Button selectWorlds;
    Button shop;
    Button continueButton;

    Image titleImage;
    Image continueImage;
    Image rapido;
    Image historia;
    Image tienda;
    Engine e;

    Game game;
    @Override
    public void init(Engine e_, Game g_) {

        e = e_;
        game = g_;


        titleImage = e.getGraphics().newImage("title.png");
        rapido = e.getGraphics().newImage("rapido.png");
        historia = e.getGraphics().newImage("historia.png");
        tienda = e.getGraphics().newImage("tienda.png");
        continueImage = e.getGraphics().newImage("play.png");

        e.getAudio().newSound("click_sound", false);

        gW = game.getGameWidth();
        gH = game.getGameHeight();
        buttonposx = gW/3;
        buttonposy = gH*6/10;
        buttonwidth = gW/3;
        buttonheight = gW/6;

        imageposx = gW/2 - (gW*2/3)/2;
        imageposy = gH/8;
        imagewidth = gW*2/3;
        imageheight = gW*2/3;

        play = new Button(buttonposx, buttonposy, buttonwidth, buttonheight, "play", true, game);
        selectWorlds = new Button(buttonposx, buttonposy + buttonheight, buttonwidth, buttonheight, "worlds", true, game);
        shop = new Button(buttonposx, buttonposy + buttonheight*2, buttonwidth, buttonheight, "shop", true, game);
        continueButton = new Button(gW/2 - (gW*2/3)/2,gH/8,gW*2/3,gW*2/3, "continue", true, game);
    }

    @Override
    public void update(float deltaTime) {
        //
    }

    @Override
    public void render(Graphics g) {
        g.setColor(PlayerState.wordColor);

        if(!game.hasContinue()) g.drawImage(titleImage, imageposx, imageposy, imagewidth, imageheight);
        else g.drawImage(continueImage, imageposx, imageposy, imagewidth, imageheight);

        g.drawImage(rapido, buttonposx, buttonposy, buttonwidth, buttonheight);
        g.drawImage(historia, buttonposx, buttonposy+ buttonheight, buttonwidth, buttonheight);
        g.drawImage(tienda, buttonposx, buttonposy+ buttonheight * 2, buttonwidth, buttonheight);
    }

    @Override
    public void handleInput(List<Input.TouchEvent> events) {
        if (!events.isEmpty()) {
            for (Input.TouchEvent ev : events) {
                play.handleInput(ev);
                selectWorlds.handleInput(ev);
                shop.handleInput(ev);
                continueButton.handleInput(ev);
            }
            events.clear();
        }
    }

    // Lista de funciones para los botones
    public void buttonFunction(String function_) {
        switch (function_) {
            case "play":
                e.getAudio().playSound("click_sound");
                game.toMenuScene();
                e.getAds().hideBanner();
                break;
            case "worlds":
                e.getAudio().playSound("click_sound");
                game.toWorldsScene();
                e.getAds().hideBanner();
                break;
            case "shop":
                e.getAudio().playSound("click_sound");
                game.toShopScene();
                e.getAds().hideBanner();
                break;
            case "continue":
                if(game.hasContinue()) {
                    game.toGameScene("continue", false);
                    e.getAds().hideBanner();
                }
                break;
        }
    }

    //Tamanos de boton e imagen
    int buttonwidth = 0;
    int buttonheight = 0;
    int buttonposx = 0;
    int buttonposy = 0;
    int imagewidth = 0;
    int imageheight = 0;
    int imageposx = 0;
    int imageposy = 0;

    //Tamaños de la pantalla de juego
    int gW = 0;
    int gH = 0;
}
