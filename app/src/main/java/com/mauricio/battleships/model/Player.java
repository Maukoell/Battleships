package com.mauricio.battleships.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private int playerNr;
    private ArrayList<Cell> shipCells;

    public Player(int playerNr) {
        this.playerNr = playerNr;
        shipCells = new ArrayList<>();
    }

    public ArrayList<Cell> getShipCells() {
        return shipCells;
    }

    public int getPlayerNr() {
        return playerNr;
    }

    public void addCell(Cell cell) {
        shipCells.add(cell);
    }
}
