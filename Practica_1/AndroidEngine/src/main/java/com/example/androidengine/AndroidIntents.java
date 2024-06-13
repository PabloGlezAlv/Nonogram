package com.example.androidengine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.engine.Intents;

public class AndroidIntents implements Intents {

    Context context;

    public AndroidIntents() {};

    //Comparte un mensaje de texto tras haber superado un nivel por los medios que permita el dispositivo
    @Override
    public void Share()
    {
        Intent intent = new Intent(Intent. ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "He superado un nuevo nivel en Nonogram! ;)");
        context.startActivity(Intent.createChooser(intent, "Compartir logro")) ;
    }

    public void setContext(Context c_) { context = c_; }
}
