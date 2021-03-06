package com.mauricio.battleships.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.mauricio.battleships.R;
import com.mauricio.battleships.model.Cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdapterBoard extends ArrayAdapter<Cell> implements Serializable {
    private static final String TAG = "AdapterBoard";
    private LayoutInflater inflater;
    private int height;

    public AdapterBoard(Context context, List<Cell> objects) {
        super(context, -1, objects);
        inflater = LayoutInflater.from(context);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<Cell> getItems () {
        ArrayList<Cell> temp = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            temp.add(this.getItem(i));
        }
        return temp;
    }

    public void onNewDataArrived(int position, Cell cell) {
        ArrayList<Cell> cells = this.getItems();
        ArrayList<Cell> cells1 = (ArrayList<Cell>) cells.clone();
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return 0;
            }

            @Override
            public int getNewListSize() {
                return 0;
            }

            @Override
            public boolean areItemsTheSame(int i, int i1) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(int i, int i1) {

                return false;
            }
        });
        //diff.dispatchUpdatesTo(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_grid_object, parent, false);
        Cell cell = getItem(position);


        Button button = view.findViewById(R.id.button_board_cell);
        Log.d(TAG, Integer.toString(button.getLayoutParams().height));
        Log.d(TAG, Integer.toString(button.getLayoutParams().width));

        if (height != 0) {
            button.getLayoutParams().height = height;
        }


        if (cell.getStatus() == Cell.CellStatus.HIT)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorHit));
        else if (cell.getStatus() == Cell.CellStatus.MISSED)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMissed));
        else if (cell.getStatus() == Cell.CellStatus.WATER)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWater));
        else
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorShip));

        return view;
    }

    public void addCells(GridView gridView, int numCells) {
        gridView.setAdapter(this);
        for (int i = 0; i < numCells; i++)
            this.add(new Cell(Cell.CellStatus.WATER, i));
    }

    public void updateGridView(GridView gridView) {
        gridView.setAdapter(this);
    }
}
