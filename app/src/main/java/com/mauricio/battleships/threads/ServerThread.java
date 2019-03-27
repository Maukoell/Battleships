package com.mauricio.battleships.threads;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.mauricio.battleships.EndActivity;
import com.mauricio.battleships.PlacingActivity;
import com.mauricio.battleships.PlayingActivity;
import com.mauricio.battleships.model.AdapterBoard;
import com.mauricio.battleships.model.Cell;
import com.mauricio.battleships.model.Game;
import com.mauricio.battleships.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerThread implements Runnable {
    private static final String TAG = "ServerThread";
    private boolean running = true;
    private String myIp;
    private static final int SERVERPORT = 4445;
    private ServerSocket serverSocket;
    private Game game = Game.getInstance();

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ServerThread() {
        super();
    }

    public void run() {
        Socket socket = null;
        game.setServerThread(this);
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
    private Game game = Game.getInstance();
    private BufferedReader input;
    private PrintWriter out;
    private ObjectOutputStream outOb;
    private ObjectInputStream in2;

    public CommunicationThread(Socket clientSocket) {

        this.clientSocket = clientSocket;

        try {
            Log.d(TAG, "getting Input Stream");
            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            outOb = new ObjectOutputStream(this.clientSocket.getOutputStream());
            in2 = new ObjectInputStream(this.clientSocket.getInputStream());
            game.setOut(out);
            game.setInput(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        boolean running = true;
        while (running) {

            try {

                String read = input.readLine();

                //updateConversationHandler.post(new updateUIThread(read));
                if (read != null ) switch (read) {
                    case "CONNECT":
                        out.println("CONNECTED");
                        Log.d(TAG, "CONNECTED");
                        Intent intent = new Intent(game.getContext(), PlacingActivity.class);
                        game.getContext().startActivity(intent);
                        break;
                    case "PLACING FINISHED":
                        game.setPlacingClient(true);
                        game.checkPlacingStatus();
                        break;
                    case "BEGIN GAME":
                        out.println("BEGIN GAME");
                        Player p2 = (Player) in2.readObject();
                        game.setP2(p2);
                        outOb.writeObject(game.getP1());
                        Intent intent1 = new Intent(game.getContext(), PlayingActivity.class);
                        game.getContext().startActivity(intent1);
                        break;
                    case "MISSED":
                        String lin = input.readLine();
                        game.getBoardP1().getItem(Integer.parseInt(lin)).setStatus(Cell.CellStatus.MISSED);
                        game.getPa().updateBoard1();
                        //game.getPa().showToastM();
                        game.getPa().mTurn();
                        break;
                    case "HIT":
                        String lin2 = input.readLine();
                        game.getBoardP1().getItem(Integer.parseInt(lin2)).setStatus(Cell.CellStatus.HIT);
                        game.getPa().updateBoard1();
                        break;
                    case "I WIN":
                        Intent intent2 = new Intent(game.getContext(), EndActivity.class);
                        intent2.putExtra("result", "You lost!");
                        game.getPa().startActivity(intent2);
                        running = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
