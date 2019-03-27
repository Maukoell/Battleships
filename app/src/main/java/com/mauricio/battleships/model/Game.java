package com.mauricio.battleships.model;

import android.content.Context;
import android.widget.Toast;

import com.mauricio.battleships.PlayingActivity;
import com.mauricio.battleships.threads.SendingThread;
import com.mauricio.battleships.threads.ServerThread;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.logging.Handler;

public class Game {
    private static Game instance;
    private AdapterBoard boardP1;
    private AdapterBoard boardP2;
    private Player p1;
    private Player p2;
    private Context context;
    private ServerThread serverThread;
    private ServerSocket serverSocket;
    private boolean placingServer = false;
    private boolean placingClient = false;
    private BufferedReader input;
    private PrintWriter out;
    private PlayingActivity pa;
    private volatile boolean update = false;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setPa(PlayingActivity pa) {
        this.pa = pa;
    }

    public void getFirstPlayer() {

        double i = Math.random() * 2;
        if (i < 1) {
            //Server Turn
            Thread t =  new Thread(new SendingThread("I START"));
            t.start();
            Toast.makeText(context, "You start", Toast.LENGTH_SHORT).show();
            pa.mTurn();
        }else {
            //Client Turn
            Thread t = new Thread(new SendingThread("YOU START"));
            t.start();
            Toast.makeText(context, "Opponent starts", Toast.LENGTH_SHORT).show();
            pa.oTurn();

        }

    }

    public PlayingActivity getPa() {
        return pa;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public static Game getInstance() {

        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public Game() {
    }

    public void checkPlacingStatus() {
        if (serverThread != null) {
            if (placingServer && placingClient) {
                // send Opponent 'BEGIN GAME'
                Thread t = new Thread(new SendingThread("BEGIN GAME"));
                t.start();
            } else if (placingServer && !placingClient) {
                Thread t = new Thread(new SendingThread("PLACING FINISHED"));
                t.start();
            }
        } else {
            if (placingServer && placingClient) {
                // send Opponent 'BEGIN GAME'
                Thread t = new Thread(new SendingThread("BEGIN GAME"));
                t.start();
            } else if (!placingServer && placingClient) {
                Thread t = new Thread(new SendingThread("PLACING FINISHED"));
                t.start();
            }
        }

    }

    public boolean isPlacingServer() {
        return placingServer;
    }

    public void setPlacingServer(boolean placingServer) {
        this.placingServer = placingServer;
    }

    public boolean isPlacingClient() {
        return placingClient;
    }

    public void setPlacingClient(boolean placingClient) {
        this.placingClient = placingClient;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public ServerThread getServerThread() {
        return serverThread;
    }

    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public AdapterBoard getBoardP1() {
        return boardP1;
    }

    public void setBoardP1(AdapterBoard boardP1) {
        this.boardP1 = boardP1;
    }

    public AdapterBoard getBoardP2() {
        return boardP2;
    }

    public void setBoardP2(AdapterBoard boardP2) {
        this.boardP2 = boardP2;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }
}
