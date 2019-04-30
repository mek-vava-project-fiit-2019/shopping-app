package com.example.shoppingapp.view;

import android.app.Activity;
import android.view.View;

public interface IDynamicGrid {

    void clearGrid();

    void addRow(View row);

    Activity getActivity();
}
