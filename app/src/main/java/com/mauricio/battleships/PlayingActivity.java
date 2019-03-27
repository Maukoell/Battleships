package com.mauricio.battleships;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mauricio.battleships.model.AdapterBoard;
import com.mauricio.battleships.model.Cell;
import com.mauricio.battleships.model.Game;
import com.mauricio.battleships.model.Player;
import com.mauricio.battleships.threads.SendingThread;

import java.util.ArrayList;


public class PlayingActivity extends AppCompatActivity {

    private Game game;
    private GridView gridP1;
    private AdapterBoard boardP1;
    private GridView gridP2;
    private AdapterBoard boardP2;
    private Player p1;
    private Player p2;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        handler = new Handler();
        game = Game.getInstance();
        game.setContext(this);
        game.setPa(this);
        p1 = game.getP1();
        p2 = game.getP2();
        gridP1 = findViewById(R.id.p1Grid);
        boardP1 = game.getBoardP1();
        boardP1.setHeight(45);
        boardP1.updateGridView(gridP1);
        gridP2 = findViewById(R.id.p2Grid);
        boardP2 = new AdapterBoard(this, new ArrayList<Cell>());
        boardP2.addCells(gridP2, 100);
        if (game.getServerThread() != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    game.getFirstPlayer();
                }
            }, 1500);


        }
    }

    public void showToastM() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(game.getContext(), "Your turn!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showToastO() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(game.getContext(), "Opponents turn!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void mTurn() {
        gridP2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell cell = (Cell) parent.getAdapter().getItem(position);
                ArrayList<Cell> al = p2.getShipCells();
                boolean hit = false;
                for (Cell c: al) {
                    if (c.getPosition() == position) {
                        c.setStatus(Cell.CellStatus.HIT);
                        al.remove(c);
                        hit = true;
                        break;
                    }
                }
                if (!hit) {
                    cell.setStatus(Cell.CellStatus.MISSED);
                    Thread t = new Thread(new SendingThread("MISSED"));
                    t.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    oTurn();
                    new Thread(new SendingThread(Integer.toString(position))).start();
                } else {
                    cell.setStatus(Cell.CellStatus.HIT);
                    Thread t = new Thread(new SendingThread("HIT"));
                    t.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new Thread(new SendingThread(Integer.toString(position))).start();
                }
                boardP2.notifyDataSetChanged();
                if (p2.getShipCells().isEmpty()) {
                    Thread t = new Thread(new SendingThread("I WIN"));
                    t.start();
                    Intent intent = new Intent(game.getContext(), EndActivity.class);
                    intent.putExtra("result", "You won!");
                    startActivity(intent);
                }
            }
        });
    }

    public void updateBoard2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boardP2.notifyDataSetChanged();
            }
        });
    }

    public void updateBoard1() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boardP1.notifyDataSetChanged();
            }
        });
    }

    public void oTurn() {
        gridP2.setOnItemClickListener(null);
    }
}
