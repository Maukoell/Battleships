package com.mauricio.battleships.model;

import java.io.Serializable;

public class Cell implements Serializable {

    private CellStatus status;
    private int position;

    public Cell(CellStatus status, int position) {
        this.status = status;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public enum CellStatus {
        WATER, HIT, SHIP, MISSED
    }
}
