package com.example.androidengine;

import android.content.Context;
import android.graphics.Typeface;

import com.example.engine.Font;

public class AndroidFont implements Font {
    public AndroidFont(String filename,float size, boolean isBold, Context context) {
        try {
            f = Typeface.createFromAsset(context.getAssets(), "data/assets/"+filename);
        } catch(Exception e) {
            e.printStackTrace();
        }
        _size = size;
        _isBold = isBold;
    }
    public void setSize(int size) {_size = size;}
    public Typeface f;
    public float _size;
    public boolean _isBold;
}
