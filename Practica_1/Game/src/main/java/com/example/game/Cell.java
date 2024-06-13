package com.example.game;

import com.example.engine.Graphics;
import com.example.engine.Input;

public class Cell {
    public enum State {BLANK, MARKED, CROSS, WRONG, COMPLETED};

    Cell(int x, int y, int side, Boolean s) {
        if(s) state_ = State.MARKED;
        else state_ = State.BLANK;
        side_ = side;
        x_ = x;
        y_ = y;
    }

    public void render(Graphics g) {
        switch (state_) {
            case BLANK:
                break;
            case MARKED:
                g.setColor(0xFF444444);
                g.fillSquare(x_, y_, side_);
                break;
            case CROSS:
                g.setColor(0xFF444444);
                g.drawLine(x_, y_, x_+side_, y_+side_);
                g.drawLine(x_+side_, y_, x_, y_ + side_);
                break;
            case WRONG:
                g.setColor(0xFFFF0000);
                g.fillSquare(x_, y_, side_);
                break;
            case COMPLETED:
                g.setColor(0xFF0000FF);
                g.fillSquare(x_, y_, side_);
                break;
        }
    }

    // Sucesion de estados por click
    public void handleInput(Input.TouchEvent ev) {
        if(ev.type == Input.TouchEvent.TouchType.longpress) {
            switch (state_) {
                case BLANK:
                    state_ = State.CROSS;
                    break;
                case MARKED:
                    state_ = State.CROSS;
                    break;
                case CROSS:
                    state_ = State.BLANK;
                    break;
            }
        } else {
            switch (state_) {
                case BLANK:
                    state_ = State.MARKED;
                    break;
                case MARKED:
                    state_ = State.BLANK;
                    break;
                case CROSS:
                    state_ = State.MARKED;
                    break;
            }
        }
    }

    // La celula gestion su transicion de estado erroneo
    // a estado marcado
    public void update(float deltaTime) {
        if(state_ == State.WRONG) {
            timer_+=deltaTime;
            if(timer_ >= cooldown_) {
                state_ = State.BLANK;
                timer_ = 0;
            }
        }
    }

    public boolean isMarked() {
        return (state_ == State.MARKED);
    }

    public void setState(State s) {
        state_ = s;
    }

    public State getState() {
        return state_;
    }

    public void setPos(int x, int y) { x_ = x; y_ = y;}
    public void setSide(int side) { side_ = side; }
    public int getX() {return x_;}
    public int getY() {return y_;}

    private State state_;
    private int side_;
    private int x_, y_;
    private float cooldown_ = 2, timer_ = 0;
}
