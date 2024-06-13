package com.example.pcengine;

import com.example.engine.Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class PCInput implements Input, MouseListener, MouseMotionListener
{

    private ArrayList<TouchEvent> events;

    public PCInput() {
        events = new ArrayList<TouchEvent>();
    }


    @Override
    public ArrayList<TouchEvent> getTouchEvents() {
        return events;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TouchEvent event = new TouchEvent(TouchEvent.TouchType.click, e.getPoint().x, e.getPoint().y, 0,0, e.getID());

        events.add(event);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
