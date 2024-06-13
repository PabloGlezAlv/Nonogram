package com.example.game;

import com.example.engine.Graphics;

public class FakeWindow {
    public FakeWindow(int width, int height) {
        x_ = 0; y_ = 0;
        w_ = width; h_ = height;
    }

    public void render(Graphics g) {
        g.setColor(0xFFFFFFFF);
        g.fillRectangle(0, 0, 400, 600);
    }

    public int getX_() {
        return x_;
    }

    public int getY_() {
        return y_;
    }

    public int getW_() {
        return w_;
    }

    public int getH_() {
        return h_;
    }

    private int x_, y_;
    private int w_, h_;

}
