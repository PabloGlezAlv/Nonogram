package com.example.pcengine;

import com.example.engine.Engine;
import com.example.game.Game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

public class PCEngine implements Engine, Runnable {

    private PCGraphics g;
    private PCInput i;
    private PCAudio a;

    private Game game;
    private JFrame frame;

    private boolean running;
    private Thread renderThread;

    public PCEngine(JFrame f)
    {
        frame = f;

        g = new PCGraphics(frame);

        i = new PCInput();

        a = new PCAudio();

        frame.addMouseListener(i);
    }

    @Override
    public void run()
    {
        BufferStrategy strategy = frame.getBufferStrategy();
        game.init();
        float lastTime = System.nanoTime();
        while(true)
        {
            //Obtencion del deltatime
            float currentTime = System.nanoTime();
            float nanoElapsedTime = currentTime - lastTime;
            float deltaTime = (float)(nanoElapsedTime / 1e9);
            lastTime = currentTime;
            do {
                do {
                    Graphics graphics = strategy.getDrawGraphics();
                    g.setGraphics(graphics);
                    game.handleInput();
                    game.update(deltaTime);
                    game.render(g);
                    graphics.dispose();
                }while(strategy.contentsRestored());
                strategy.show();
            } while(strategy.contentsLost());
        }
    }

    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    @Override
    public PCGraphics getGraphics() {
        return g;
    }

    @Override
    public PCInput getInput() {
        return i;
    }

    @Override
    public PCAudio getAudio() {
        return a;
    }

    @Override
    public InputStream openInputStream(String filename) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream("data/assets/"+filename);
        } catch (Exception e) {
            System.err.println("Error cargando el archivo: " + e);
            return null;
        }
        return is;
    }

    public void setGame(Game g) {
        game = g;
    }

}
