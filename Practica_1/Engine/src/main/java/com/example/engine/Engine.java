package com.example.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public interface Engine {

    public Graphics getGraphics();

    public Input getInput();

    public Audio getAudio();

    public Ads getAds();

    public Intents getIntents();

    boolean firtTime();

    File createFile(String filename) throws IOException;
    InputStream openInputStreamFromMobile(String filename) throws IOException;
    InputStream openInputStream(String filename) throws IOException;
    OutputStreamWriter openOutputStream(String filename) throws IOException;
    FileOutputStream getFileOutputStream(String filename) throws IOException;
    FileInputStream getFileInputStream(String filename) throws IOException;
}
