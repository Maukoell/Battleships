package com.mauricio.battleships;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SendForConnection implements Runnable {
    private String myIp;
    private String otherIp;
    private int port = 4445;

    public SendForConnection(String myIp, String otherIp) {
        super();
        this.myIp = myIp;
        this.otherIp = otherIp;
    }

    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket(port);
            InetAddress serverAddr = InetAddress.getByName(otherIp);
            byte[] buf = (myIp).getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, port);
            udpSocket.send(packet);
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "IO Error:", e);
        }
    }
}
