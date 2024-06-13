package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Image;
import com.example.engine.Input;

import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game {

    Engine e;
    String level_;
    GameScene gs;
    InitScene is;
    MenuScene ms;
    WorldsScene ws;
    ChooseLevelScene cs;
    shopScene ss;

    public FakeWindow fw_;
    int gameW = 400;
    int gameH = 600;
    Image backGround;

    int scene_id;

    public int getGameWidth() {return gameW;}
    public int getGameHeight() {return gameH;}

    int previousScene_id;
    private boolean hasContinue = false;

    public void init() {
        previousScene_id = -1;
        scene_id = 0;

        initPlayerState();
        checkContinue();

        fw_ = new FakeWindow(gameW, gameH);

        is = new InitScene();
        is.init(e, this);

        ms = new MenuScene();

        gs = new GameScene();

        ws = new WorldsScene();

        cs = new ChooseLevelScene();

        ss = new shopScene();

        setBackGround();
    }

    public void setBackGround() {
        if(PlayerState.darkmode && PlayerState.backGround == 0)
            backGround = e.getGraphics().newImage("backgroundDark.png");
        else
            backGround = e.getGraphics().newImage("background" + PlayerState.backGround +".png");
    }

    public void update(float deltaTime) {
        switch (scene_id)
        {
            case 0:
                is.update(deltaTime);
            break;
            case 1:
                ms.update(deltaTime);
                break;
            case 2:
                gs.update(deltaTime);
                break;
            case 3:
                ws.update(deltaTime);
                break;
            case 4:
                cs.update(deltaTime);
                break;
            case 5:
                ss.update(deltaTime);
                break;
            default:
                System.out.println("Error update: Wrong scene id." +  scene_id);
        }
    }

    public void render(Graphics g) {
        switch (scene_id)
        {
            case 0:
                is.render(g);
                break;
            case 1:
                ms.render(g);
                break;
            case 2:
                gs.render(g);
                break;
            case 3:
                ws.render(g);
                break;
            case 4:
                cs.render(g);
                break;
            case 5:
                ss.render(g);
                break;
            default:
                System.out.println("Error render: Wrong scene id." +  scene_id);
        }
    }

    public void drawBackGround(Graphics g) {
        g.drawImage(backGround, 0, 0, g.getWidth(), g.getHeight());
    }

    public void handleInput(List<Input.TouchEvent> events) {

        switch (scene_id)
        {
            case 0:
                is.handleInput(events);
                break;
            case 1:
                ms.handleInput(events);
                break;
            case 2:
                gs.handleInput(events);
                break;
            case 3:
                ws.handleInput(events);
                break;
            case 4:
                cs.handleInput(events);
                break;
            case 5:
                ss.handleInput(events);
                break;
            default:
                System.out.println("Error handle: Wrong scene id." +  scene_id);
        }
    }

    public void setEngine(Engine en) {
        e = en;
    }

    public void buttonFunction(String function_) {
        switch (scene_id)
        {
            case 0:
                is.buttonFunction(function_);
                break;
            case 1:
                ms.buttonFunction(function_);
                break;
            case 2:
                gs.buttonFunction(function_);
                break;
            case 3:
                ws.buttonFunction(function_);
                break;
            case 4:
                cs.buttonFunction(function_);
                break;
            case 5:
                ss.buttonFunction(function_);
                break;
            default:
                System.out.println("Error buttons: Wrong scene id." +  scene_id);
        }
    }

    public void toInitScene() {
        is.init(e, this);
        scene_id = 0;
        e.getAds().showBanner();
    }

    public void toMenuScene()
    {
        previousScene_id = scene_id;
        ms.init(e, this);
        scene_id = 1;
    }

    public void toGameScene(String level, Boolean lastLevel) {
        previousScene_id = scene_id;
        level_ = level;
        gs.init(e, this);
        gs.setTypeLevel(lastLevel);
        scene_id = 2;
    }

    public void toWorldsScene(){
        previousScene_id = scene_id;
        ws.init(e, this);
        scene_id = 3;
    }
    public void toChooseLevel(String world){
        previousScene_id = scene_id;
        cs.setWorld(world);
        cs.init(e, this);
        scene_id = 4;
    }

    public void toShopScene()
    {
        previousScene_id = scene_id;
        ss.init(e, this);
        scene_id = 5;
    }

    public void setMode(boolean dark)
    {
        if(dark)
        {
            PlayerState.darkmode = true;
            PlayerState.wordColor = 0xFFFFFFFF;
        }
        else
        {
            PlayerState.darkmode = false;
            PlayerState.wordColor = 0xFF000000;
        }

        setBackGround();
    }

    private void initPlayerState() {
        if(!e.firtTime())
        {
            try {
                FileInputStream file = e.getFileInputStream("progress.ser");
                ObjectInputStream in = new ObjectInputStream(file);

                String reader = (String)in.readObject();

                String aux[] = reader.split("\n");

                int num = Integer.parseInt(aux[0]);
                PlayerState.numCoins = num;

                int levels = Integer.parseInt(aux[1]);
                PlayerState.levelsPassed = levels;

                for(int i = 0; i <  PlayerState.backgroundUnlocked.length; i++)
                {
                    int locked = Integer.parseInt(aux[2 + i]);
                    PlayerState.backgroundUnlocked[i] = locked;
                }
                for(int i = 0; i < PlayerState.wordColorUnlocked.length; i++)
                {
                    int lockedc = Integer.parseInt(aux[2 + PlayerState.backgroundUnlocked.length + i]);
                    PlayerState.wordColorUnlocked[i] = lockedc;
                }

                in.close();
                file.close();
            }
            catch (IOException | ClassNotFoundException e) {
                System.out.println("Progreso no encontrado o CORRUPTO.");
                e.printStackTrace();
            }
        }
        else
        {
            PlayerState.numCoins = 50;

            PlayerState.levelsPassed = 34;

            PlayerState.backgroundUnlocked[0] = 1;
            for(int i = 1; i <  PlayerState.backgroundUnlocked.length; i++)
            {
                PlayerState.backgroundUnlocked[i] = -1;
            }
            PlayerState.wordColorUnlocked[0] = 1;
            for(int i = 1; i < PlayerState.wordColorUnlocked.length; i++)
            {
                PlayerState.wordColorUnlocked[i] = -1;
            }
        }
        System.out.println(PlayerState.numCoins );
    }

    public void savePlayerState() {
        try {
            FileOutputStream file = e.getFileOutputStream("progress.ser");
            ObjectOutputStream out = new ObjectOutputStream(file) ;

            String object = "";

            object += (PlayerState.numCoins) + "\n";
            object +=(PlayerState.levelsPassed) + "\n";

            for(int i = 0; i <  PlayerState.backgroundUnlocked.length; i++)
            {
                object +=(PlayerState.backgroundUnlocked[i]) + "\n";
            }

            for(int i = 0; i <  PlayerState.wordColorUnlocked.length; i++)
            {
                object +=(PlayerState.wordColorUnlocked[i]) + "\n";
            }


            out.writeObject(object) ;
            out.close() ;
            file.close() ;

            System.out.println("Informacion guardada");
        }
        catch (Exception e) {
            System.out.println("Progreso no encontrado o CORRUPTO.");
            e.printStackTrace();
        }
    }

    //Metodo que comprueba si hay archivo de tablero guardado para continuar jugando
    private void checkContinue() {
        try {
            FileInputStream file = e.getFileInputStream("savedBoard.ser");
            ObjectInputStream in = new ObjectInputStream(file);

            String reader = (String)in.readObject();

            String aux[] = reader.split("\n");

            int num = Integer.parseInt(aux[0]);
            //Si el primer valor es un 0, es que no hay nivel que rescatar
            if(num == 0) hasContinue = false;
            else hasContinue = true;

            in.close();
            file.close();
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Tablero no encontrado.");
            hasContinue = false;
        }
    }

    public void onPause() {
        if(scene_id == 2) gs.saveBoard(false);
        savePlayerState();
    }

    public boolean hasContinue() { return hasContinue; }
    public void setHasContinue(boolean aux) { hasContinue = aux; }

    public FakeWindow getFakeWindow() {
        return fw_;
    }

    public void getReward() {
        gs.extraLife();
    }

    public void addCoins(int amount){
        PlayerState.numCoins += amount;
        savePlayerState();
    }
}
