package com.example.pclauncher;

import com.example.game.Game;
import com.example.pcengine.PCEngine;
import java.awt.*;
import java.awt.image.BufferStrategy;

import com.example.pcengine.PCGraphics;
import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args)
    {
        PCEngine e;
        Game g;

        JFrame frame = new JFrame("Nonogram");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 600);
        // Esto sirve para que el frame no pinte con dibujado pasivo
        frame.setIgnoreRepaint(true);
        frame.createBufferStrategy(2);

        g = new Game();
        e = new PCEngine(frame);
        g.setEngine(e);
        e.setGame(g);

        e.resume();
    }

}