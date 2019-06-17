package sk.mek.shoppingapp.controller.viewmanager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import sk.mek.shoppingapp.model.entity.CartItem;
import sk.mek.shoppingapp.view.DetailedProductView;
import sk.mek.shoppingapp.view.IDynamicGrid;

import java.util.ArrayList;

public class TableManager {
    public static void createRow(View a, View b, IDynamicGrid iDynamicGrid){

        /* Create a new row to be added. */
        TableRow tableRow = new TableRow(iDynamicGrid.getActivity());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        a.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.addView(a);
        if(b!= null) {
            b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.addView(b);
        } else {
            tableRow.addView(new LinearLayout(iDynamicGrid.getActivity()));
        }

        iDynamicGrid.addRow(tableRow);
    }

    public static void createSingle(View a, IDynamicGrid iDynamicGrid){

        TableRow tableRow = new TableRow(iDynamicGrid.getActivity());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        a.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.addView(a);

        iDynamicGrid.addRow(tableRow);
    }

    public static CartItem[] readTable(LinearLayout productTableLayout){
        ArrayList<CartItem> allCartItems = new ArrayList<>();

        for (int i = 0; i < productTableLayout.getChildCount(); i++) {
            DetailedProductView detailedProductView = (DetailedProductView)productTableLayout.getChildAt(i);

            allCartItems.add(detailedProductView.getCartItem());
        }

        CartItem[] cartItemArray = new CartItem[allCartItems.size()];
        return allCartItems.toArray(cartItemArray);
    }
}
