package com.example.pcengine;

import com.example.engine.Font;
import com.example.engine.Image;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;


public class PCGraphics implements com.example.engine.Graphics {

    public PCGraphics(JFrame frame) {
        frame_ = frame;
    }

    private Graphics g;
    AffineTransform _state;

    private JFrame frame_;

    public void setGraphics(Graphics gr) {
        g = gr;
    }

    @Override
    public Image newImage(String name) {
        return new PCImage(name);
    }

    @Override
    public Font newFont(String filename, int size, boolean isBold) {
        return new PCFont(filename, size, isBold);
    }

    @Override
    public void setFont(Font font) {
        PCFont f = (PCFont) font;
        g.setFont(f.getFont());
    }

    @Override
    public void clear(int color) {
        setColor(color);

        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void translate(int x, int y) {

        g.translate(x, y);
    }

    @Override
    public void scale(int x, int y) {
        ((Graphics2D) g).scale(x, y);
    }

    @Override
    public void restore() {
        ((Graphics2D) g).setTransform(_state);
    }

    @Override
    public void save() {
        _state = ((Graphics2D) g).getTransform();
    }

    @Override
    public void drawImage(Image image, int x, int y, int x2, int y2) {
        PCImage img = (PCImage) image;
        g.drawImage(img.getImage(), x, y,x2, y2, null);
    }

    @Override
    public void setColor(int color) {
        g.setColor(new Color(color));
    }

    @Override
    public void fillSquare(int cx, int cy, int side) {

        g.fillRect(cx, cy, side, side);
    }

    @Override
    public void fillRectangle(int cx, int cy, int sidex, int sidey) {

        g.fillRect(cx, cy, sidex, sidey);
    }

    @Override
    public void drawSquare(int cx, int cy, int side) {
        g.drawRect(cx, cy, side, side);
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h) {
        g.drawRect(x, y, w, h);
    }

    @Override
    public void drawLine(int initX, int initY, int endX, int endY) {

        g.drawLine (initX, initY, endX, endY);
    }

    @Override
    public void drawText(String text, int x, int y) {
        g.drawString(text, x, y);
    }

    public void drawCenteredText(String text, int rectx, int recty, int rectw, int recth) {
        FontMetrics fm = g.getFontMetrics();

        int fontWidth = fm.stringWidth(text);
        int fontHeight = fm.getAscent();

        int posx = rectx+rectw/2-fontWidth/2;
        int posy = recty+recth/2+fontHeight/3;

        g.drawString(text, posx, posy);
    }

    @Override
    public int getWidth() {
        return frame_.getWidth();
    }

    @Override
    public int getHeight() {
        return frame_.getHeight();
    }
}
