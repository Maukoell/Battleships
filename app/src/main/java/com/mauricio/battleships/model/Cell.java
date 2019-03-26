package com.mauricio.battleships.model;

public class Cell {

    private statusInts status = statusInts.WATER;

    public enum statusInts {
        WATER, HIT, SHIP
    }
}
