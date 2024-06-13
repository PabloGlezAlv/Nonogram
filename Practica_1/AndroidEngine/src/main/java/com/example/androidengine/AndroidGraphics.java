package com.example.androidengine;

import com.example.engine.Font;
import com.example.engine.Graphics;
import com.example.engine.Image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class AndroidGraphics implements com.example.engine.Graphics {
    Canvas c;
    Paint p;
    Context context;

    public AndroidGraphics() {
        p = new Paint();
    }

    @Override
    public Image newImage(String name) {
        return new AndroidImage(name, context.getAssets());
    }

    @Override
    public void drawImage(Image image, int x, int y, int x2, int y2) {
        AndroidImage img = (AndroidImage)image;
        Rect r = new Rect(x, y, x+x2, y+y2);
        Rect d = new Rect(0,0, img.getWidth(), img.getHeight());
        c.drawBitmap(img.getImage(), d, r, p);
    }

    @Override
    public Font newFont(String filename, int size, boolean isBold) {
        return new AndroidFont(filename, size, isBold, context);
    }

    public void setFont(Font font) {
        AndroidFont f = (AndroidFont) font;
        p.setTypeface(f.f);
        p.setFakeBoldText(f._isBold);
        p.setTextSize(f._size);
    }

    @Override
    public void clear(int color) {
        setColor(color);
        fillRectangle(0, 0, getWidth(),getHeight());
    }

    @Override
    public void translate(int x, int y) {
        c.translate(x, y);
    }

    @Override
    public void scale(float x, float y) {
        c.scale(x, y);
    }

    @Override
    public void restore() {
        c.restore();
    }

    @Override
    public void save() {
        c.save();
    }

    @Override
    public void setColor(int color) {
        p.setColor(color);
    }

    @Override
    public void fillSquare(int cx, int cy, int side) {
        p.setStyle(Paint.Style.FILL);
        c.drawRect(cx, cy, cx+side, cy+side, p);
        p.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void fillRectangle(int cx, int cy, int sidex, int sidey) {
        p.setStyle(Paint.Style.FILL);
        c.drawRect(cx, cy, sidex, sidey, p);
        p.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void drawSquare(int cx, int cy, int side) {
        drawRectangle(cx, cy, side, side);
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h) {
        drawLine(x, y, x+w, y);
        drawLine(x+w, y, x+w, y+h);
        drawLine(x+w, y+h, x, y+h);
        drawLine(x, y+h, x, y);
    }

    @Override
    public void drawLine(int initX, int initY, int endX, int endY) {
        c.drawLine(initX, initY,  endX,  endY, p);
    }

    @Override
    public void drawText(String text, int x, int y) {

        p.setStyle(Paint.Style.FILL);
        c.drawText(text, x, y, p);
        p.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void drawCenteredText(String text, int rectx, int recty, int rectw, int recth) {
        Paint.FontMetrics fm = p.getFontMetrics();

        int fontWidth = (int)p.measureText(text);
        int fontHeight = (int)(fm.descent - fm.ascent);

        int posx = rectx+rectw/2-fontWidth/2;
        int posy = recty+recth/2+fontHeight/3;

        p.setStyle(Paint.Style.FILL);
        c.drawText(text, posx, posy, p);
        p.setStyle(Paint.Style.STROKE);
    }

    @Override
    public int getWidth() {
        return c.getWidth();
    }

    @Override
    public int getHeight() {
        return c.getHeight();
    }

    public void setCanvas(Canvas can) {
        c = can;
    }

    public void setContext(Context c_) { context = c_; }
}
