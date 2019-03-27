package com.mauricio.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.mauricio.battleships.model.AdapterBoard;
import com.mauricio.battleships.model.Cell;
import com.mauricio.battleships.model.Game;
import com.mauricio.battleships.model.Player;

import java.util.ArrayList;

public class PlacingActivity extends AppCompatActivity {
    private static final String TAG = "PlacingActivity";
    private GridView placingGrid;
    private AdapterBoard board;
    private ShipPlacement shipPlacement;
    private TextView shipString;
    private int counter;
    private Player p1;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placing);
        game = Game.getInstance();
        game.setContext(this);
        p1 = new Player(1);
        game.setP1(p1);
        shipString = findViewById(R.id.shipString);
        shipPlacement = ShipPlacement.TWO;
        updateShipPlacementString();
        Log.d(TAG, "vor erstellung");
        placingGrid = findViewById(R.id.placingGrid);
        Log.d(TAG, "placingGrid ");
        board = new AdapterBoard(this, new ArrayList<Cell>());
        Log.d(TAG, "AdapterBoard erstellt");
        board.addCells(placingGrid, 100);
        Log.d(TAG, "Zellen erstellt");
        addItemListener();
    }

    public void addItemListener() {
        placingGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell cell = (Cell) parent.getAdapter().getItem(position);
                if (cell.getStatus() == Cell.CellStatus.WATER) {
                    cell.setStatus(Cell.CellStatus.SHIP);
                    board.notifyDataSetChanged();
                    counter += 1;
                    Log.d(TAG, "OnItemListener");
                    checkCounter();
                    p1.addCell(cell);
                }
            }
        });
    }

    public void removeItemListener() {
        placingGrid.setOnItemClickListener(null);
    }

    public void checkCounter() {
        switch (shipPlacement) {
            case FIVE:
                if (counter == 5) {
                    shipPlacement = ShipPlacement.FINISHED;
                    counter = 0;
                    updateShipPlacementString();
                    Log.d(TAG, "FIVE");
                }
                break;
            case FOUR:
                if (counter == 4) {
                    shipPlacement = ShipPlacement.FIVE;
                    counter = 0;
                    updateShipPlacementString();
                    Log.d(TAG, "FOUR");
                }
                break;
            case THREE:
                if (counter == 3) {
                    shipPlacement = ShipPlacement.FOUR;
                    counter = 0;
                    updateShipPlacementString();
                    Log.d(TAG, "THREE");
                }
                break;
            case TWO:
                if (counter == 2) {
                    shipPlacement = ShipPlacement.THREE;
                    counter = 0;
                    updateShipPlacementString();
                    Log.d(TAG, "TWO");
                }
                break;
        }
    }

    public void updateShipPlacementString() {
        switch (shipPlacement) {
            case TWO:
                shipString.setText("Platzieren Sie ein Schiff mit 2 Feldern");
                Log.d(TAG, "Set 2");
                break;
            case THREE:
                shipString.setText("Platzieren Sie ein Schiff mit 3 Feldern");
                Log.d(TAG, "Set 3");
                break;
            case FOUR:
                shipString.setText("Platzieren Sie ein Schiff mit 4 Feldern");
                Log.d(TAG, "Set 4");
                break;
            case FIVE:
                shipString.setText("Platzieren Sie ein Schiff mit 5 Feldern");
                Log.d(TAG, "Set 5");
                break;
            case FINISHED:
                removeItemListener();
                shipString.setText("Auf Gegner warten");
                game.setBoardP1(board);
                if (game.getServerThread() != null) {
                    game.setPlacingServer(true);
                    game.checkPlacingStatus();
                }else {
                    game.setPlacingClient(true);
                    game.checkPlacingStatus();
                }
                break;
        }
    }

    public enum ShipPlacement {
        TWO, THREE, FOUR, FIVE, FINISHED
    }
}
