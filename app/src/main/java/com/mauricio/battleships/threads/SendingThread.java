package com.mauricio.battleships.threads;

import android.util.Log;

import com.mauricio.battleships.model.Game;

import java.io.PrintWriter;

public class SendingThread implements Runnable {
    private static final String TAG = "SendingThread";
    private Game game = Game.getInstance();
    private PrintWriter out = game.getOut();
    private String msg;

    public SendingThread(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        Log.d(TAG, "Sending message: " + msg);
            out.println(msg);
    }
}
