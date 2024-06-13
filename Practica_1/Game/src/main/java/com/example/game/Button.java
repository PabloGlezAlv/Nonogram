package com.example.game;

import com.example.engine.Graphics;
import com.example.engine.Input;

public class Button {
    // Creacion de boton. El boolean sides sirve para dibujar el contenedor
    // del boton, util para debug
    Button(int x, int y, int w, int h, String func, boolean sides, Game game) {
        game_ = game;
        function_ = func;
        drawSides = sides;
        x_ = x;
        y_ = y;
        w_ = w;
        h_ = h;
    }

    public void render(Graphics g) {
        if(drawSides) {
            g.setColor(0xFF000000);
            g.drawRectangle(x_, y_, w_, h_);
        }
    }

    public boolean handleInput(Input.TouchEvent ev) {
        if((ev.posX > x_ && ev.posX < x_ + w_) && (ev.posY > y_ && ev.posY < y_ + h_)) {
            game_.buttonFunction(function_);
            return false;
        }
        return true;
    }

    public void setPosition(int x, int y) {
        x_ = x;
        y_ = y;
    }

    public void setSizes(int w, int h) {
        w_ = w;
        h_ = h;
    }

    public int getX_(){
        return x_;
    }

    public int getY_() {
        return y_;
    }


    private boolean drawSides;
    private int x_, y_;
    private int w_, h_;
    private String function_;
    private Game game_;
}
