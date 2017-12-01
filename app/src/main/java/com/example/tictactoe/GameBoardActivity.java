package com.example.tictactoe;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameBoardActivity extends AppCompatActivity {
    private static final String TAG = GameBoardActivity.class.getSimpleName();

    public final static String EXTRA_STRING_PLAYER1_NAME = "EXTRA_STRING_PLAYER1_NAME";
    public final static String EXTRA_STRING_PLAYER2_NAME = "EXTRA_STRING_PLAYER2_NAME";
    public final static String EXTRA_BOOLEAN_PLAYER1_GO_FIRST = "EXTRA_BOOLEAN_PLAYER1_GO_FIRST";
    public String player1;
    public String player2;
    public boolean playerOnePlaysX;
    public boolean computerPlays;
    private String currentPlayer;
    private AlertDialog.Builder builder;
    private int p1score, p2score;
    private int turnNumber;

    @BindView(R.id.txtMsgTop)
    TextView txtMsgTop;
    @BindView(R.id.txtMsgBottom)
    TextView txtMsgBottom;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn6)
    Button btn6;
    @BindView(R.id.btn7)
    Button btn7;
    @BindView(R.id.btn8)
    Button btn8;
    @BindView(R.id.btn9)
    Button btn9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        ButterKnife.bind(this);
        playerOnePlaysX = true;
        computerPlays = false;

        // parsing Intent
        player1 = getIntent().getStringExtra(EXTRA_STRING_PLAYER1_NAME);
        player2 = getIntent().getStringExtra(EXTRA_STRING_PLAYER2_NAME);
        boolean playerOneGoFirst = getIntent().getBooleanExtra(EXTRA_BOOLEAN_PLAYER1_GO_FIRST, true);

        if (playerOneGoFirst) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
        // Needed for score board
        builder = new AlertDialog.Builder(this);
        checkForWin(); //Run once in the beginning
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9})
    void btnOnClick(View view) {
        if (view instanceof Button) {
            Button b = (Button) view;
            String buttonText = b.getText().toString();
            if (!(buttonText.equalsIgnoreCase("x") || buttonText.equalsIgnoreCase("o"))) {

                if (currentPlayer.equalsIgnoreCase(player1) && playerOnePlaysX) {
                    b.setText("X");
                } else if (currentPlayer.equalsIgnoreCase(player1) && !playerOnePlaysX) {
                    b.setText("O");
                } else if (currentPlayer.equalsIgnoreCase(player2) && playerOnePlaysX) {
                    b.setText("O");
                } else if (currentPlayer.equalsIgnoreCase(player2) && !playerOnePlaysX) {
                    b.setText("X");
                } else {
                    txtMsgTop.setText("Error occured, please, restart.");
                }
                checkForWin();
            } else {
                Toast.makeText(getApplicationContext(), "Please, select blank spot!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void clearGameBoard() {
        int white = Color.parseColor("#FFFFFF");
        btn1.setText("");
        btn1.setBackgroundColor(white);
        btn2.setText("");
        btn2.setBackgroundColor(white);
        btn3.setText("");
        btn3.setBackgroundColor(white);
        btn4.setText("");
        btn4.setBackgroundColor(white);
        btn5.setText("");
        btn5.setBackgroundColor(white);
        btn6.setText("");
        btn6.setBackgroundColor(white);
        btn7.setText("");
        btn7.setBackgroundColor(white);
        btn8.setText("");
        btn8.setBackgroundColor(white);
        btn9.setText("");
        btn9.setBackgroundColor(white);
        txtMsgBottom.setText("");
        turnNumber = 0;
    }

    private void checkForWin() {
        String winningCombo;
        // Keep track if game is over (tie)
        turnNumber++;

        if (currentPlayer.equalsIgnoreCase(player1)) {
            if (playerOnePlaysX) {
                winningCombo = "XXX";
            } else {
                winningCombo = "OOO";
            }
        } else if (currentPlayer.equalsIgnoreCase(player2)) {
            if (playerOnePlaysX) {
                winningCombo = "OOO";
            } else {
                winningCombo = "XXX";
            }
        } else {

            txtMsgTop.setText("Error occured, please, restart.");
            winningCombo = "ERROR";
        }
        HashMap<Integer, Button[]> winningPattern = new HashMap<>();
        winningPattern.put(0, new Button[]{btn1, btn2, btn3});
        winningPattern.put(1, new Button[]{btn4, btn5, btn6});
        winningPattern.put(2, new Button[]{btn7, btn8, btn9});
        winningPattern.put(3, new Button[]{btn1, btn4, btn7});
        winningPattern.put(4, new Button[]{btn2, btn5, btn8});
        winningPattern.put(5, new Button[]{btn3, btn6, btn9});
        winningPattern.put(6, new Button[]{btn1, btn5, btn9});
        winningPattern.put(7, new Button[]{btn3, btn5, btn7});

        ArrayList possibleCombinations = new ArrayList<String>();
        possibleCombinations.add(btn1.getText().toString() + btn2.getText().toString() + btn3.getText().toString());
        possibleCombinations.add(btn4.getText().toString() + btn5.getText().toString() + btn6.getText().toString());
        possibleCombinations.add(btn7.getText().toString() + btn8.getText().toString() + btn9.getText().toString());
        possibleCombinations.add(btn1.getText().toString() + btn4.getText().toString() + btn7.getText().toString());
        possibleCombinations.add(btn2.getText().toString() + btn5.getText().toString() + btn8.getText().toString());
        possibleCombinations.add(btn3.getText().toString() + btn6.getText().toString() + btn9.getText().toString());
        possibleCombinations.add(btn1.getText().toString() + btn5.getText().toString() + btn9.getText().toString());
        possibleCombinations.add(btn3.getText().toString() + btn5.getText().toString() + btn7.getText().toString());
        int winCombo = 9;
        for (int i = 0; i < 8; i++) {
            if (winningCombo.equalsIgnoreCase(possibleCombinations.get(i).toString())) {
                winCombo = i;
                break;
            }
        }

        if (winCombo == 9) {
            // This happens if no winner.
            if (turnNumber == 9) {
                txtMsgBottom.setText("Tie Game!");
                this.displayScore(null);
            }

            if (currentPlayer.equalsIgnoreCase(player1)) {
                currentPlayer = player2;
            } else {
                currentPlayer = player1;
            }

            if (!computerPlays && playerOnePlaysX && currentPlayer.equalsIgnoreCase(player1)) {
                txtMsgTop.setText(player1 + "'s turn (x)");
            } else if (!computerPlays && !playerOnePlaysX && currentPlayer.equalsIgnoreCase(player1)) {
                txtMsgTop.setText(player1 + "'s turn (o)");
            } else if (!computerPlays && playerOnePlaysX && currentPlayer.equalsIgnoreCase(player2)) {
                txtMsgTop.setText(player2 + "'s turn (o)");
            } else if (!computerPlays && !playerOnePlaysX && currentPlayer.equalsIgnoreCase(player2)) {
                txtMsgTop.setText(player1 + "'s turn (x)");
            } else if (computerPlays && playerOnePlaysX) {
                txtMsgTop.setText("Computer's turn (x)");
                //TODO invoke computer play method
            } else if (computerPlays && playerOnePlaysX) {
                txtMsgTop.setText("Computer's turn (o)");
                //TODO invoke computer play method
            } else {
                txtMsgTop.setText("Error occured, please, restart.");
            }
        } else {

            txtMsgBottom.setText(currentPlayer + " won!");

            Button[] flashButtons = winningPattern.get(winCombo);

            for (int x = 2; x >= 0; x--) {
                flashButtons[x].setBackgroundColor(Color.parseColor("#FF0000"));
            }

            this.displayScore(currentPlayer);
        }
    }

    private void displayScore(String winner) {
        // Add points for current winning player
        if (player1.equals(winner))
            p1score++;
        else if (player2.equals(winner))
            p2score++;

        // Display score so-far
        builder.setTitle(txtMsgBottom.getText());
        builder.setMessage(player1 + "'s score = " + p1score + "\n" + player2
                + "'s score = " + p2score);

        // Add the buttons
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Clear the game board ...
                clearGameBoard();
            }
        });
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish(); // Goes back to Welcome screen
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
