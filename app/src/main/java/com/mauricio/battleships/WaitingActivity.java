package com.mauricio.battleships;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.mauricio.battleships.model.Game;
import com.mauricio.battleships.threads.ServerThread;

import java.net.ServerSocket;

public class WaitingActivity extends AppCompatActivity {

    private static final String TAG = "WaitingActivity";
    private String myIp;
    private Game game;
    private ServerSocket serverSocket;

    Handler updateConversationHandler;

    Thread serverThread = null;

    private static final int SERVERPORT = 4445;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        game = Game.getInstance();
        game.setContext(this);
        myIp = getIntent().getStringExtra("ipAddress");
        Log.d(TAG, "getting TextView");
        TextView tx = findViewById(R.id.ipText);
        Log.d(TAG, tx.toString());
        tx.setText(myIp);
        Log.d(TAG, "starting Thread");
        serverThread = new Thread(new ServerThread());
        Log.d(TAG, "Thread created");
        serverThread.start();
        Log.d(TAG, "Thread started");
    }

}