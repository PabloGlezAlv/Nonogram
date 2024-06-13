package com.example.engine;

public interface Graphics {

    Image newImage(String name);

    Font newFont(String filename, int size, boolean isBold);

    void setFont(Font font);

    void clear(int color);

    void translate(int x, int y);

    void scale(float x, float y);

    void restore();

    void save();

    void drawImage(Image image, int x, int y, int x2, int y2);

    void setColor(int color);

    void fillSquare(int cx, int cy, int side);

    void fillRectangle(int cx, int cy, int sidex, int sidey);

    void drawSquare(int cx, int cy, int side);

    void drawRectangle(int x, int y, int w, int h);

    void drawLine(int initX, int initY, int endX, int endY);

    void drawText(String text, int x, int y);

    void drawCenteredText(String text, int rectx, int recty, int rectw, int recth);

    int getWidth();

    int getHeight();
}
