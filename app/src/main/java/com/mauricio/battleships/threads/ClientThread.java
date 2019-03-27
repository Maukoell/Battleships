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
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
        private static final String TAG = "SendTCP";
        private String opIp;
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private ObjectOutputStream outOb;
        private Game game = Game.getInstance();
        private ObjectInputStream in2;

        public ClientThread(String opIp) {
            this.opIp = opIp;
        }

        @Override
        public void run() {
            try {
                this.socket = new Socket(opIp, 4445);
                String estabConnection = "CONNECT";
                in2 = new ObjectInputStream(this.socket.getInputStream());
                outOb = new ObjectOutputStream(this.socket.getOutputStream());
                out = new PrintWriter(this.socket.getOutputStream(),true);
                out.println(estabConnection);
                game.setOut(out);
                game.setInput(in);


                Log.d(TAG, "Nachricht versendet");
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                boolean running = true;
                String line = null;
                while (running) {
                    line = in.readLine();
                    if (line != null) {
                        switch (line) {
                            case "CONNECTED":
                                Intent intent = new Intent(game.getContext(), PlacingActivity.class);
                                game.getContext().startActivity(intent);
                                break;
                            case "PLACING FINISHED":
                                game.setPlacingServer(true);
                                game.checkPlacingStatus();
                                break;
                            case "BEGIN GAME":
                                outOb.writeObject(game.getP1());
                                Player p2 = (Player) in2.readObject();
                                game.setP2(p2);
                                Intent intent1 = new Intent(game.getContext(), PlayingActivity.class);
                                game.getContext().startActivity(intent1);
                                break;
                            case "MISSED":
                                String lin = in.readLine();
                                game.getBoardP1().getItem(Integer.parseInt(lin)).setStatus(Cell.CellStatus.MISSED);
                                game.getPa().updateBoard1();
                                game.getPa().mTurn();
                                break;
                            case "HIT":
                                String lin2 = in.readLine();
                                game.getBoardP1().getItem(Integer.parseInt(lin2)).setStatus(Cell.CellStatus.HIT);
                                game.getPa().updateBoard1();

                                break;
                            case "I START":
                                Looper.prepare();
                                game.getPa().oTurn();
                                break;
                            case "YOU START":
                                Looper.prepare();
                                game.getPa().mTurn();
                                break;
                            case "I WIN":
                                Intent intent2 = new Intent(game.getContext(), EndActivity.class);
                                intent2.putExtra("result", "You lost!");
                                game.getPa().startActivity(intent2);
                                running = false;
                    }
                }}
                Log.d(TAG, "Nachricht erhalten");
                Log.d(TAG, line);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

