package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Image;
import com.example.engine.Input;

import java.util.List;

public class ChooseLevelScene implements Scene {

    Button volverButton;

    Image volverButtonImage;

    Image[] levels;
    Image levelslocked;

    Button[] levelButton;

    Engine e;

    Game g;

    @Override
    public void init(Engine e_, Game g_) {
        levelButton = new Button[16];
        levels = new Image[16];
        e = e_;
        g = g_;

        gW = g.getGameWidth();
        gH = g.getGameHeight();

        worldsImageW = gW/5;
        worldsImageH = gW/5;

        volverButton = new Button(20, 20, gW/10, gW/10, "back", true, g);
        volverButtonImage =  e.getGraphics().newImage("backButton.png");
        levelslocked = e.getGraphics().newImage("locked.png");

        for(int i = 0; i <levelButton.length;i++)
        {
            levels[i] = e.getGraphics().newImage(world_ + ".png");
        }

        for(int i = 0; i <levelButton.length;i++)
        {
            levelButton[i] = new Button(gW/2-worldsImageW*2 + worldsImageW * (i%numColumnas), gH/2-worldsImageH*2 + worldsImageH * (i/numFilas), worldsImageW, worldsImageH, "Categorias/"+world_ +"/"+ (i+1), true, g);
        }
    }

    @Override
    public void update(float deltaTime) {
        //
    }

    @Override
    public void render(Graphics g) {
        g.setColor(PlayerState.wordColor);

        g.drawImage(volverButtonImage, 20, 20, gW/10, gW/10);

        for(int i = 0; i <numFilas;i++)
        {
            for(int j = 0; j <numColumnas;j++)
            {
                if(numColumnas*i + j <= passed && passed >= 0)
                {
                    g.drawImage(levels[numColumnas*i + j], gW/2-worldsImageW*2 + worldsImageW * i ,
                            gH/2-worldsImageH*2 + worldsImageH * j, worldsImageW, worldsImageH);
                }
                else
                {
                    g.drawImage( levelslocked, gW/2-worldsImageW*2 + worldsImageW * i ,
                            gH/2-worldsImageH*2 + worldsImageH * j, worldsImageW, worldsImageH);
                }
            }
        }
    }

    @Override
    public void handleInput(List<Input.TouchEvent> events) {
        if (!events.isEmpty()) {
            for (Input.TouchEvent ev : events) {
                for(int i = 0; i <= passed && i < 16;i++)
                {
                    levelButton[i].handleInput(ev);
                }
                volverButton.handleInput(ev);
            }
            events.clear();
        }
    }

    // Lista de funciones para los botones
    public void buttonFunction(String function_) {
        switch (function_) {
            case "back":
                e.getAudio().playSound("click_sound");
                g.toWorldsScene();
                break;
            default:
                e.getAudio().playSound("click_sound");

                int nLevel = passed +1;
                System.out.println(function_);
                System.out.println("Categorias/"+ lastWorld_ +"/" + nLevel);
                if(function_.equals("Categorias/"+ lastWorld_ +"/" + nLevel)) //Check if its las level
                {
                    System.out.println("Last level");
                    g.toGameScene(function_, true);
                }
                else g.toGameScene(function_, false);
                break;
        }
    }

    public void setWorld(String worldName) {

        world_ = worldName;

        switch (worldName)
        {
            case "a":
                world = 0;
                break;
            case "ag":
                world = 1;
                break;
            case "f":
                world = 2;
                break;
            case "t":
                world = 3;
                break;

        }
        lastWorld = PlayerState.levelsPassed / 16;

        switch (lastWorld)
        {
            case 0:
                lastWorld_ = "a";
                break;
            case 1:
                lastWorld_ = "ag";
                break;
            case 2:
                lastWorld_ = "f";
                break;
            case 3:
                lastWorld_ = "t";
                break;

        }

        if(world < lastWorld)
        {
            passed = 16;
        }
        else
        {
            System.out.println(world);
            System.out.println(PlayerState.levelsPassed);
            passed = PlayerState.levelsPassed - world * 20;
            System.out.println(passed);
        }
    }


    int worldsImageW = 0;
    int worldsImageH = 0;

    int gW = 0;
    int gH = 0;

    int numColumnas = 4;
    int numFilas = 4;

    String world_;
    String lastWorld_;

    int passed = 0;
    int world = 0;
    int lastWorld;
}
