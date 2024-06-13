package com.example.androidengine;


import static android.hardware.Sensor.TYPE_LIGHT;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import com.example.engine.Engine;
import com.example.engine.Input;
import com.example.game.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class AndroidEngine implements Engine, Runnable {

    private SurfaceHolder holder;

    private Thread renderThread;

    private Canvas c;

    private boolean surfaceReady = false;

    private boolean drawingActive = false;

    private AndroidGraphics g;
    private AndroidInput i;
    private AndroidAudio a;
    private AndroidIntents ai;
    private AndroidAds ads;

    private Game game;
    private boolean running;

    private SurfaceView v;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private float maxValue;

    private boolean darkMode = false;

    public AndroidEngine(SurfaceView view, SensorManager sManager)
    {
        v = view;
        holder = view.getHolder();

        g = new AndroidGraphics();

        i = new AndroidInput();

        a = new AndroidAudio();

        ai = new AndroidIntents();

        v.setOnTouchListener(i);

        sensorManager = sManager;
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);

        if(lightSensor == null)
        {
            System.out.println("Movil sin sensor de luz");
        }

        maxValue = lightSensor.getMaximumRange();

        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float light = sensorEvent.values[0];

                if(light <= maxValue/2 && !darkMode)
                {
                    darkMode = !darkMode;
                    game.setMode(true);
                }
                else if(light > maxValue/2 && darkMode)
                {
                    darkMode = !darkMode;
                    game.setMode(false);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    @Override
    public AndroidGraphics getGraphics() {
        return g;
    }

    @Override
    public AndroidInput getInput() {
        return i;
    }

    @Override
    public AndroidAudio getAudio() {
        return a;
    }

    @Override
    public AndroidIntents getIntents() {
        return ai;
    }

    public AndroidAds getAds() { return ads; }
    public void setAds(AndroidAds ad) { ads = ad; }

    //Comprueba a traves de la existencia del archivo de progreso si
    //es la primera vez que se abre la app
    @Override
    public boolean firtTime() {
        File f = new File(v.getContext().getFilesDir() + "/progress.ser");
        return !f.exists();
    }

    //Crea un archivo en la carpeta local del dispositivo
    @Override
    public File createFile(String filename) throws IOException {

        File f = new File(v.getContext().getFilesDir() + "/"+filename);;
        if(!f.exists())
            f.mkdir();
        return f;
    }

    //Abre un archivo local del dispositivo
    @Override
    public InputStream openInputStreamFromMobile(String filename) throws IOException{
        File f = new File(v.getContext().getFilesDir() + "/"+filename);
        return new FileInputStream(f);
    }

    //Abre un archivo de la apk
    @Override
    public InputStream openInputStream(String filename) throws IOException {
        return v.getContext().getAssets().open("data/assets/"+filename);
    }

    @Override
    public OutputStreamWriter openOutputStream(String filename) throws IOException {
        return new OutputStreamWriter(v.getContext().openFileOutput(filename, Context.MODE_PRIVATE));
    }

    //Abre un archivo local del dispositivo
    @Override
    public FileOutputStream getFileOutputStream(String filename) throws IOException {
        return new FileOutputStream(v.getContext().getFilesDir() + "/"+filename);
    }

    //Abre un archivo local del dispositivo
    @Override
    public FileInputStream getFileInputStream(String filename) throws IOException {
        return new FileInputStream(v.getContext().getFilesDir() + "/"+filename);
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            throw new RuntimeException("run() should not be called directly");
        }
        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(running && v.getWidth() == 0);

        g.setContext(v.getContext());

        a.setContext(v.getContext());

        ai.setContext(v.getContext());

        ads.init();
        game.init();
        float lastTime = System.nanoTime();
        while(true) {
            if(holder == null) return;

            while (!holder.getSurface().isValid())
                ;

            c = holder.lockCanvas();
            g.setCanvas(c);

            // Obtencion del deltatime
            float currentTime = System.nanoTime();
            float nanoElapsedTime = currentTime - lastTime;
            float deltaTime = (float)(nanoElapsedTime / 1e9);
            lastTime = currentTime;
            try {
                if(c != null) {
                    game.update(deltaTime);
                    handleInput();
                    render();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

    private void handleInput() {
        List<Input.TouchEvent> eventList = getInput().getTouchEvents();
        if(eventList.isEmpty()) return;

        float windowW = g.getWidth();
        float windowH = g.getHeight();
        float gameW = game.getGameWidth();
        float gameH = game.getGameHeight();
        float gameWindowX;
        float gameWindowY;
        float gameWindowW;
        float gameWindowH;
        float prop;

        for(Input.TouchEvent ev: eventList) {
            float touchX = ev.posX;
            float touchY = ev.posY;

            if(windowW >= windowH/3*2) {
                prop = windowH/gameH;
                gameWindowY = 0;
                gameWindowX = (int)(windowW/2.0-(gameW*prop)/2.0);
            } else {
                prop = windowW/gameW;
                gameWindowX = 0;
                gameWindowY = (int)(windowH/2.0-(gameH*prop)/2.0);
            }
            gameWindowW = (int)(gameW*prop);
            gameWindowH = (int)(gameH*prop);

            if(touchX > gameWindowX && touchX < gameWindowX+gameWindowW && touchY > gameWindowY && touchY < gameWindowY+gameWindowH) {
                float relativeX = touchX-gameWindowX;
                float relativeY = touchY-gameWindowY;
                ev.posX = (int)(gameW*(relativeX/gameWindowW));
                ev.posY = (int)(gameH*(relativeY/gameWindowH));
            }
            else {
                ev.posX = -1;
                ev.posY = -1;
            }
        }
        game.handleInput(eventList);
    }

    private void render() {
        float windowW = g.getWidth();
        float windowH = g.getHeight();
        float gameW = game.getGameWidth();
        float gameH = game.getGameHeight();
        float prop = 0;

        g.clear(0xFFFFFFFF);
        game.drawBackGround(g);
        if(windowW >= windowH/3*2) {
            prop = windowH/gameH;
            g.translate((int)(windowW/2.0-(gameW*prop)/2.0),0);
        } else {
            prop = windowW/gameW;
            g.translate(0, (int)(windowH/2.0-(gameH*prop)/2.0));
        }
        g.scale(prop, prop);

        game.render(g);
    }

    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;

            sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);

            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    //Metodo que notfica al juego si se ha salido de la aplicacion
    public void onPause()
    {
        game.onPause();
    }

    //Metodo de obtencion de recompensa de anuncio
    public void getReward() {
        game.getReward();
    }

    public void setGame(Game g) {
        game = g;
    }

    public void OnNotificationClicked (int amount){
        game.addCoins(amount);
    }
}
