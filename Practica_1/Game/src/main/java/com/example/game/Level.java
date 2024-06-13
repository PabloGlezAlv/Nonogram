package com.example.game;

import java.util.ArrayList;

public class Level {

    Level() {

    }

    public void init(int w, int h, ArrayList<ArrayList<Boolean>> lMap,  ArrayList<ArrayList<Integer>> topN,  ArrayList<ArrayList<Integer>> leftN) {

        width = w;
        height = h;

        levelMap = lMap;
        topNumbers = topN;
        leftNumbers = leftN;

    }


    private int width = 0;
    private int height = 0;

    private ArrayList<ArrayList<Boolean>> levelMap;
    private ArrayList<ArrayList<Integer>> topNumbers;
    private ArrayList<ArrayList<Integer>> leftNumbers;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<ArrayList<Boolean>> getLevelMap() {
        return levelMap;
    }
    public ArrayList<ArrayList<Integer>> getTopNumbers() {return topNumbers;}
    public ArrayList<ArrayList<Integer>> getLeftNumbers() {return leftNumbers;}
}
