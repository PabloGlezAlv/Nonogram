package com.example.pcengine;

import com.example.engine.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PCImage implements Image {
    public PCImage(String name) {
        _image = null;
        try {
            _image = ImageIO.read(new File("data/assets/"+name));
        } catch (IOException e) {
            System.err.println("Error cargando la imagen: " + e);
            return;
        }
    }

    @Override
    public int getWidth() {
        return _image.getWidth();
    }

    @Override
    public int getHeight() {
        return _image.getHeight();
    }

    public BufferedImage getImage() {
        return _image;
    }

    private BufferedImage _image;
}
