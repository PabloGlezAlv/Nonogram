package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Image;
import com.example.engine.Input;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
public class GameScene implements Scene{

    Board board_;
    LevelLoader levelLoader;
    Level newLevel;

    Engine e;
    Game g_;

    Button volverButton;
    Button recoverLife;
    Button shareTweet;

    Image shareTweetImage;
    Image recoverImage;
    Image volverButtonImage;
    Image lifeImage;
    Image lostLifeImage;
    Image coinsImage;
    Image failedImage;

    Font numbersFont;
    Font infoFont;

    int fontSize;

    int visibleamount = 0;
    int extracoins; //random 0 o 1
    boolean completed = false;
    boolean failed;

    boolean lastLevel;

    @Override
    public void init(Engine e_, Game g) {

        completed = false;
        failed = false;
        e = e_;
        g_ =g;

        gW = g.getGameWidth();
        gH = g.getGameHeight();

        extracoins = 20 + (int) (Math.random()*50); //random 0 o 1
        //e.getAudio().newSound("background_musi", true);
        //e.getAudio().playSound("background_musi");
        levelLoader = new LevelLoader(e);

        board_ =  new Board(gW, gH);

        recoverImage = e.getGraphics().newImage("ad.png");
        recoverLife = new Button(20, gH - 30, gW/10, gW/10, "extralife", false, g);

        shareTweetImage = e.getGraphics().newImage("share.png");
        shareTweet = new Button(20, gH - 30, gW/10, gW/10, "share", false, g);

        volverButton = new Button(20, 20, gW/10, gW/10, "back", true, g);
        volverButtonImage =  e.getGraphics().newImage("backButton.png");

        lifeImage = e.getGraphics().newImage("life.png");
        lostLifeImage = e.getGraphics().newImage("lostlife.png");
        coinsImage = e.getGraphics().newImage("gems.png");

        failedImage = e.getGraphics().newImage("youdied.png");

        //Forzado elegir el 5x5
        setBoardSize(g_.level_);
        board_.setLevel(newLevel);
        board_.init();

        numbersFont = e.getGraphics().newFont("JosefinSans-Bold.ttf", gW/20, false);
        infoFont = e.getGraphics().newFont("JosefinSans-Bold.ttf", gW/20, false);

        visibleamount = 0;
    }
    @Override
    public void update(float deltaTime) {
        board_.update(deltaTime);
        if(completed || failed) return;

        checkResults();
        if(board_.getLifes() == 0) {
            failed = true;
            board_.setFailed(true);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setFont(numbersFont);
        board_.render(g);
        if(failed) g.drawImage(failedImage, 0, gH/2-100, gW, 200);
        else if(completed)
        {
            g.setColor(0xFF00FF00);
            g.drawCenteredText("ENHORABUENA!", 0, 0, gW, gH/3);

            g.setColor(0xFF00FFAA);

            g.drawImage(shareTweetImage, 20, gH-30, gW/10, gW/10);

            if(visibleamount < extracoins) visibleamount++;

            g.drawCenteredText("+ " + visibleamount, gW/2-gW/8,9*gH/10, gW/8, gW/8);
            g.drawImage(coinsImage, gW/2+gW/8,9*gH/10,gW/8, gW/8);
        }
        g.setColor(PlayerState.wordColor);

        g.drawImage(volverButtonImage, 20, 20, gW/10, gW/10);

        if(completed || failed) return;

        g.drawImage(recoverImage, 20, gH-30, gW/10, gW/10);
        drawLifes(g);
    }
    @Override
    public void handleInput(List<Input.TouchEvent> events) {
        if(!events.isEmpty()) {
            for (Input.TouchEvent ev : events) {
                if(!volverButton.handleInput(ev) && completed)
                    completed = false;
                board_.handleInput(ev);
                if(!completed || failed)  recoverLife.handleInput(ev);
                if(completed && !failed)   shareTweet.handleInput(ev);
            }
            events.clear();
        }
    }

    // Lista de funciones para los botones
    public void buttonFunction(String function_) {
        switch (function_) {
            case "back":
                e.getAudio().playSound("click_sound");
                //e.getAudio().stopSound("background_musi");
                g_.toInitScene();
                break;
            case "extralife":
                e.getAudio().playSound("click_sound");
                e.getAds().showRewardedAd();
                break;
            case "share":
                e.getIntents().Share();
        }
    }


    public void setTypeLevel(boolean lastLevelPassed)
    {
        lastLevel = lastLevelPassed;
    }

    public void setBoardSize(String level) {

        if(level == "random")
        {
            newLevel = levelLoader.loadLevelRandom();
        }
        else if(level == "continue") {
            newLevel = levelLoader.loadLevelContinue();
        }
        else
        {
            newLevel = levelLoader.loadLevel(level+".txt");
        }


        board_ =  new Board(gW, gH);
        board_.setLevel(newLevel);
        board_.init();
        if(level == "continue") {
            board_.setMarkedBoard(levelLoader.getLevelContinueMarked());
        }
    }

    public void extraLife()
    {
        int lifes = board_.getLifes();
        if(lifes < 3 && PlayerState.numCoins >= 25)
        {
            System.out.println("Vida recuperada");
            board_.setLifes(lifes + 1);
            PlayerState.numCoins -= 25;
        }
    }

    private void checkResults()
    {
        completed = board_.checkResults();
        if(completed) {
            if(lastLevel && PlayerState.levelsPassed < 80)
            {
                PlayerState.levelsPassed++;
            }
            PlayerState.numCoins += extracoins;
            g_.savePlayerState();
            saveBoard(completed);
            g_.setHasContinue(false);
        }
    }

    private void drawLifes(Graphics g) {
        int lifes = board_.getLifes();
        boolean aux = false;

        for(int i = 0; i < 3 ;i++)
        {
            if(lifes == 3-i || aux) //Vida llena
            {
                g.drawImage(lifeImage, gW/2+i*(gW/8 + gW/25),9*gH/10,gW/8, gW/8);
                aux = true;
            }
            else
            {
                g.drawImage(lostLifeImage, gW/2+i*(gW/8 + gW/25),9*gH/10,gW/8,gW/8);
            }
        }
    }

    public void saveBoard(boolean completed) {
            try {
                FileOutputStream file = e.getFileOutputStream("savedBoard.ser");
                ObjectOutputStream out = new ObjectOutputStream(file);

                String object = "";

                if(completed) object += "0\n";
                else {
                    object += newLevel.getWidth() + "\n";
                    object += newLevel.getHeight() + "\n";

                    for (int i = 0; i < board_.getSize(); i++) {
                        for (int j = 0; j < board_.getSize(); j++) {
                            if(newLevel.getLevelMap().get(i).get(j))
                                object += "1 ";
                            else object += "0 ";
                        }
                        object += "\n";
                    }

                    for (int i = 0; i < board_.getSize(); i++) {
                        for (int j = 0; j < board_.getSize(); j++) {
                            if (board_.getCellBoard().get(j).get(i).isMarked()) {
                                object += "1 ";
                            } else object += "0 ";
                        }
                        object += "\n";
                    }
                }

                out.writeObject(object);
                out.close();
                file.close();

                System.out.println("Tablero guardado");
            } catch (Exception e) {
                System.out.println("Progreso no encontrado o CORRUPTO.");
                e.printStackTrace();
            }
    }

    int gW = 0;
    int gH = 0;
}
