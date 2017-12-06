package com.fhda.cs64a.tictactoe;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    static public List<Character> convertToArrayList(Button[] buttons) {

        ArrayList<Character> btnList = new ArrayList<>();
        for (Button btn : buttons) {
            btnList.add(btn.getTag().toString().charAt(0));
        }
        return btnList;
    }
}
