package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Image;
import com.example.engine.Input;

import java.util.List;

public class shopScene implements Scene {

    private enum stateShop
    {
        info, bought, confirmBought, notEnoughCoins, changeBackground, wordsColorChange
    }
    Button[] backGroundColor;
    Button[] wordsColor;
    Button volverButton;

    Image coinsImage;
    Image volverButtonImage;
    Image[] backGround;

    Image notBuy;

    Font numbersFont;
    Font infoFont;
    Font infoFontSmall;
    int fontSize;
    int temporalColor;

    Engine e;

    Game g;

    @Override
    public void init(Engine e_, Game g_) {

        e = e_;
        g = g_;

        backGroundColor = new Button[4];
        wordsColor = new Button[4];
        backGround = new Image[4];

        estadoCompra = stateShop.info;

        gW = g.getGameWidth();
        gH = g.getGameHeight();

        buttonwidth = gW*3/10;
        buttonheight = gW*3/20;
        buttonposx = gW/2 - buttonwidth;
        buttonposy = gH*6/10 - buttonheight*3;

        imageposx = gW/2 - (gW*2/3)/2;
        imageposy = gH/5;
        imagewidth = gW*2/3;
        imageheight = gW*2/3;

        volverButton = new Button(20, 20, gW/10, gW/10, "back", true, g);

        for(int i = 0; i < backGroundColor.length;i++)
        {
            backGroundColor[i] = new Button(buttonposx, buttonposy + (buttonheight + 40) * i, buttonwidth, buttonheight, "colors" + i, true, g);
        }

        for(int i = 0; i < wordsColor.length;i++)
        {
            wordsColor[i] = new Button(gW*11/20, buttonposy + (buttonheight + 40) * i, buttonwidth, buttonheight, "words" + i, true, g);
        }

        volverButtonImage =  e.getGraphics().newImage("backButton.png");
        coinsImage = e.getGraphics().newImage("gems.png");
        notBuy = e.getGraphics().newImage("locked.png");
        for(int i = 0; i < wordsColor.length;i++)
        {
            backGround[i] = e.getGraphics().newImage("background" + i +".png");
        }

        fontSize = gW/7;
        numbersFont = e.getGraphics().newFont("JosefinSans-Bold.ttf", fontSize, false);
        infoFont = e.getGraphics().newFont("JosefinSans-Bold.ttf", fontSize/2, false);
        infoFontSmall = e.getGraphics().newFont("JosefinSans-Bold.ttf", fontSize/3, false);
        visibleamount = PlayerState.numCoins;
        temporalColor = PlayerState.wordColor;
    }

    @Override
    public void update(float deltaTime) {
        //
    }

    @Override
    public void render(Graphics g) {
        DrawBackgroundButtons(g);
        DrawWordsButtons(g);

        g.drawImage(volverButtonImage, 20, 20, gW/10, gW/10);

        if(visibleamount > PlayerState.numCoins) visibleamount--;
        g.setFont(numbersFont);
        g.drawCenteredText( ""+visibleamount, gW*7/10, 20, gW/10,gW/10);
        g.drawImage(coinsImage, gW*9/10, 20, gW/10,gW/10);

        DrawMessage(g);
        DrawHeader(g);
    }

    private void DrawMessage(Graphics g)
    {
        switch (estadoCompra)
        {
            case info:
                g.setFont(infoFont);
                g.drawCenteredText("Elige lo que quieras comprar", 0, 0, gW, gH/3);
                break;
            case bought:
                g.setFont(infoFont);
                g.drawCenteredText("Compra realizada con exito", 0, 0, gW, gH/3);
                break;
            case confirmBought:
                g.setFont(infoFontSmall);
                g.drawCenteredText("Pulsa otra vez el boton para confirmar la compra", 0, 0, gW, gH/3);
                break;
            case notEnoughCoins:
                g.setFont(infoFont);
                g.drawCenteredText("No tienes suficientes monedas", 0, 0, gW, gH/3);
                break;
            case changeBackground:
                g.setFont(infoFont);
                g.drawCenteredText("Fondo cambiado con exito", 0, 0, gW, gH/3);
                break;
            case wordsColorChange:
                g.setFont(infoFont);
                g.drawCenteredText("Color de letras cambiado con exito", 0, 0, gW, gH/3);
                break;
        }
    }

    private void DrawBackgroundButtons(Graphics g)
    {
        g.setColor(temporalColor);
        for(int i = 0; i < backGroundColor.length;i++)
        {
            backGroundColor[i].render(g);
            g.drawImage(backGround[i], buttonposx, buttonposy + (buttonheight + 40) * i, buttonwidth, buttonheight);
            if(PlayerState.backgroundUnlocked[i] == -1)
            {
                g.drawImage(notBuy, buttonposx + (buttonwidth - buttonheight) /2, buttonposy + (buttonheight + 40) * i, buttonheight, buttonheight);
            }
        }
    }

    private void DrawWordsButtons(Graphics g)
    {
        g.setFont(infoFont);
        for(int i = 0; i < wordsColor.length;i++)
        {
            g.setColor(colors[i]);
            g.drawCenteredText("Ejemplo", gW*11/20 + (buttonwidth - buttonheight) /2, buttonposy + (buttonheight + 40) * i, buttonheight, buttonheight);
        }

        for(int i = 0; i < wordsColor.length;i++)
        {
            wordsColor[i].render(g);
            if(PlayerState.wordColorUnlocked[i] == -1)
            {
                g.drawImage(notBuy, gW*11/20 + (buttonwidth - buttonheight) /2, buttonposy + (buttonheight + 40) * i, buttonheight, buttonheight);
            }
        }
    }

    private void DrawHeader(Graphics g)
    {
        g.setFont(infoFont);
        g.drawCenteredText( "Fondos", buttonposx, buttonposy-buttonheight, buttonwidth, buttonheight);
        g.drawCenteredText( "Letra", gW*11/20, buttonposy-buttonheight, buttonwidth, buttonheight);

        g.drawCenteredText( "50", buttonposx-buttonwidth/2-20, buttonposy-buttonheight, buttonwidth, buttonheight);
        g.drawCenteredText( "30", gW*11/20+buttonwidth/2, buttonposy-buttonheight, buttonwidth, buttonheight);

        g.drawImage(coinsImage, buttonposx-buttonwidth/2, buttonposy-buttonheight*3/4, buttonheight/2, buttonheight/2);
        g.drawImage(coinsImage, gW*11/20+buttonwidth+20, buttonposy-buttonheight*3/4, buttonheight/2, buttonheight/2);
    }

    @Override
    public void handleInput(List<Input.TouchEvent> events) {
        if (!events.isEmpty()) {
            for (Input.TouchEvent ev : events) {
                volverButton.handleInput(ev);
                for(int i = 0; i < backGroundColor.length;i++)
                {
                    backGroundColor[i].handleInput(ev);
                }
                for(int i = 0; i < wordsColor.length;i++)
                {
                    wordsColor[i].handleInput(ev);
                }
            }
            events.clear();
        }
    }

    // Lista de funciones para los botones
    public void buttonFunction(String function_) {
        switch (function_) {
            case "colors0":
                e.getAudio().playSound("click_sound");
                unlockBackground(50, 0);
                break;
            case "colors1":
                e.getAudio().playSound("click_sound");
                unlockBackground(50, 1);
                break;
            case "colors2":
                e.getAudio().playSound("click_sound");
                unlockBackground(50, 2);
                break;
            case "colors3":
                e.getAudio().playSound("click_sound");
                unlockBackground(50, 3);
                break;
            case "words0":
                e.getAudio().playSound("click_sound");
                unlockWordsColor(30, 0,0xFF000000);
                break;
            case "words1":
                e.getAudio().playSound("click_sound");
                unlockWordsColor(30, 1,0xFFFF0000);
                break;
            case "words2":
                e.getAudio().playSound("click_sound");
                unlockWordsColor(30, 2,0xFF0000FF);
                break;
            case "words3":
                e.getAudio().playSound("click_sound");
                unlockWordsColor(30, 3,0xFFAAAAAA);
                break;
            case "back":
                e.getAudio().playSound("click_sound");
                g.toInitScene();
                break;
        }
    }

    private void unlockBackground(int price, int unlock)
    {
        if(unlock != lastClickBackGround &&  PlayerState.backgroundUnlocked[unlock] == -1)
        {
            if(price <= PlayerState.numCoins)
            {
                estadoCompra = stateShop.confirmBought;
                lastClickBackGround = unlock;
                g.setBackGround();
            }
            else
            {
                estadoCompra = stateShop.notEnoughCoins;
                lastClickBackGround = -1;
            }
        }
        else if(PlayerState.backgroundUnlocked[unlock] == -1)
        {
            estadoCompra = stateShop.bought;
            PlayerState.backGround = unlock;
            PlayerState.numCoins -= price;
            PlayerState.backgroundUnlocked[unlock] = 1;
            g.savePlayerState();
            g.setBackGround();
            System.out.println("Compra realizada");
        }
        else //Fondo ya desbloqueado
        {
            estadoCompra = stateShop.changeBackground;
            PlayerState.backGround = unlock;
            g.setBackGround();
            System.out.println("Fondo cambiado");
        }
    }

    private void unlockWordsColor(int price, int unlock, int color)
    {
        System.out.println(unlock);
        System.out.println(lastClickWords);
        System.out.println(PlayerState.wordColorUnlocked[unlock]);
        if(unlock != lastClickWords &&  PlayerState.wordColorUnlocked[unlock] == -1)
        {
            if(price <= PlayerState.numCoins)
            {
                estadoCompra = stateShop.confirmBought;

                lastClickWords = unlock;
                temporalColor = color;
                System.out.println("Primer click");
            }
            else
            {
                estadoCompra = stateShop.notEnoughCoins;
                lastClickWords = -1;
                System.out.println("Sin monedas ");
            }
        }
        else if(PlayerState.wordColorUnlocked[unlock] == -1)
        {
            estadoCompra = stateShop.bought;
            PlayerState.numCoins -= price;
            PlayerState.wordColor = color;
            PlayerState.wordColorUnlocked[unlock] = 1;
            g.savePlayerState();
            System.out.println("Compra realizada word");
        }
        else //Fondo ya desbloqueado
        {
            estadoCompra = stateShop.wordsColorChange;
            PlayerState.wordColor = color;
            System.out.println("Letra cambiada");
        }
    }

    int buttonwidth = 0;
    int buttonheight = 0;
    int buttonposx = 0;
    int buttonposy = 0;
    int imagewidth = 0;
    int imageheight = 0;
    int imageposx = 0;
    int imageposy = 0;

    int colors[] = { 0xFF000000, 0xFFFF0000, 0xFF0000FF, 0xFFAAAAAA };

    int gW = 0;
    int gH = 0;

    int visibleamount;

    int lastClickBackGround = -1;
    int lastClickWords = -1;
    stateShop estadoCompra;
}
