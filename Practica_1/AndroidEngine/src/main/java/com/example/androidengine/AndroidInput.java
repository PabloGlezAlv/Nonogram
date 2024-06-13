package com.example.androidengine;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class AndroidInput implements com.example.engine.Input, View.OnTouchListener {

    private ArrayList<TouchEvent> events;

    public AndroidInput() {
        events = new ArrayList<TouchEvent>();
    }

    @Override
    public ArrayList<TouchEvent> getTouchEvents() {
        return events;
    }

    final Handler handler = new Handler();
    //Metodo que se manda con un delay y, si se llega a ejecutar, manda un evento de click largo
    Runnable longRun = new Runnable() {
        @Override
        public void run() {
            TouchEvent event = new TouchEvent(TouchEvent.TouchType.longpress,(int) longPosX, (int) longPosY, 0,0, id);
            events.add(event);
            longPressed = true;
        }
    };

    float longPosX, longPosY;
    int id;
    boolean longPressed = false;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        view.performClick();

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
            //Se manda ejecutar un metodo con un delay determinado, en este caso, 1000ms
                handler.postDelayed(longRun, 1000);
                longPosX = motionEvent.getX();
                longPosY = motionEvent.getY();
                id = motionEvent.getDeviceId();
                longPressed = false;
                break;

            case MotionEvent.ACTION_UP:
            //Si se levanta el dedo, se cancela la ejecucion del metodo lanzado y se manda evento de click normal
                handler.removeCallbacks(longRun);
                if(!longPressed) {
                    TouchEvent event = new TouchEvent(TouchEvent.TouchType.click, (int) motionEvent.getX(), (int) motionEvent.getY(), 0, 0, motionEvent.getDeviceId());
                    events.add(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                break;

            case MotionEvent.ACTION_POINTER_UP:

                break;

            default:
                return false;
        }

        return true;
    }
}
