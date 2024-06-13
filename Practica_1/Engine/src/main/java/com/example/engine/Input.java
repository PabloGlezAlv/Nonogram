package com.example.engine;

import java.util.ArrayList;
import java.util.List;

public interface Input {

    public class TouchEvent {

        public enum TouchType{
            click, longpress
        }

        public int posX;
        public int posY;

        public int movX;
        public int movY;

        public int id;

        public TouchType type;

        public TouchEvent(TouchType t, int x, int y, int deltaX, int deltaY, int identification){
            posX = x;
            posY = y;
            movX = deltaX;
            movY = deltaY;

            id = identification;
            type = t;
        }


    }

    ArrayList<TouchEvent> getTouchEvents();
}
