package com.example.pcengine;

import com.example.engine.Font;

import java.io.File;

public class PCFont implements Font {
    public PCFont(String is, float _size, boolean _isBold) {
        java.awt.Font auxFont;
        try
        {
            auxFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("data/assets/"+ is));
        }
        catch(Exception e)
        {
            System.err.println("Error cargando la fuente: " + e);
            return;
        }

        isBold = _isBold;
        if (isBold)
            f = auxFont.deriveFont(java.awt.Font.BOLD, _size);
        else
            f = auxFont.deriveFont(_size);
    }

    public void setSize(int size)
    {
        size_ = size;
        java.awt.Font auxFont = f;
        if (isBold)
            f = auxFont.deriveFont(java.awt.Font.BOLD, size_);
        else
            f = auxFont.deriveFont(java.awt.Font.TRUETYPE_FONT, size_);
    }

    //Getter de la fuente
    public java.awt.Font getFont() { return f; }

    public java.awt.Font f;

    int size_;
    boolean isBold;
}
