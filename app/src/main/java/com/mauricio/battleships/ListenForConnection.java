package com.mauricio.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ListenForConnection implements Runnable {
    private int port = 4445;
    private String text;
    private PlayActivity activity;

    public ListenForConnection(PlayActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void run() {
        boolean run = true;
        while (run) {
            try {
                DatagramSocket udpSocket = new DatagramSocket(port);
                byte[] message = new byte[8000];
                DatagramPacket packet = new DatagramPacket(message,message.length);
                Log.i("UDP client: ", "about to wait to receive");
                udpSocket.receive(packet);
                text = new String(message, 0, packet.getLength());
                Log.d("Received data", text);
            }catch (IOException e) {
                Log.e("error: ", "UDP client has IOException", e);
                run = false;
            }
        }
        activity.launchWaitingActivity();
    }


}
