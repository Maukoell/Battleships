package com.mauricio.battleships;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitingActivity extends AppCompatActivity {

    private static final String TAG = "WaitingActivity";
    private String myIp;

    private ServerSocket serverSocket;

    Handler updateConversationHandler;

    Thread serverThread = null;

    private static final int SERVERPORT = 4445;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
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

    @Override
    protected void onStop() {
        super.onStop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerThread implements Runnable {
        private static final String TAG = "ServerThread";
        private boolean running = true;
        public void run() {
            Socket socket = null;
            try {
                Log.d(TAG, "creating Socket");
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "bevor While");
            while (running) {
                try {

                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {
        private static final String TAG = "CommunicationThread";
        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {
                Log.d(TAG, "getting Input Stream");
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {


            while (!Thread.currentThread().isInterrupted()) {

                try {

                    String read = input.readLine();

                    if (read != null ){
                        Log.d(TAG, read);
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                        out.println(read);
                        //updateConversationHandler.post(new updateUIThread(read));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}