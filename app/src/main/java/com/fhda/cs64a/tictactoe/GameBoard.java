package com.fhda.cs64a.tictactoe;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.fhda.cs64a.tictactoe.Utils.convertToArrayList;

public class GameBoard extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GameBoard.class.getSimpleName();
    public static final String EXTRA_STRING_PLAYER1_NAME = "EXTRA_STRING_PLAYER1_NAME";
    public static final String EXTRA_STRING_PLAYER2_NAME = "EXTRA_STRING_PLAYER2_NAME";

    public static final String EXTRA_BOOLEAN_PLAYER1_GO_FIRST = "EXTRA_BOOLEAN_PLAYER1_GO_FIRST";
    public static final String COMPUTER_NAME = "Computer";
    public String player1 = "";
    public String player2 = COMPUTER_NAME;
    public boolean playerOnePlaysX = true;
    private String currentPlayer = player2;
    private HiScoreDbAdapter hiScoreDbAdapter;
    private Handler handler = null;

    List<List<Character>> winningPatterns;
    private static ArrayList<Character> player1Path;
    private static ArrayList<Character> player2Path;

    private TextView txtMsgTop;
    private TextView txtMsgBottom;
    private TableLayout btnTable;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private static Button[] btnArray = null;

    private AlertDialog.Builder builder;
    private int p1score, p2score, gamesPlayed;
    private static String availableButtons = "123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        // Create High Score database handle
        hiScoreDbAdapter = new HiScoreDbAdapter(this);
        txtMsgTop = (TextView) findViewById(R.id.txtMsgTop);
        txtMsgBottom = (TextView) findViewById(R.id.txtMsgBottom);

        btnTable = (TableLayout) findViewById(R.id.btnTable);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);

        btnArray = new Button[]{btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        winningPatterns = new ArrayList<>();
        winningPatterns.add(convertToArrayList(new Button[]{btn1, btn2, btn3}));
        winningPatterns.add(convertToArrayList(new Button[]{btn4, btn5, btn6}));
        winningPatterns.add(convertToArrayList(new Button[]{btn7, btn8, btn9}));
        winningPatterns.add(convertToArrayList(new Button[]{btn1, btn4, btn7}));
        winningPatterns.add(convertToArrayList(new Button[]{btn2, btn5, btn8}));
        winningPatterns.add(convertToArrayList(new Button[]{btn3, btn6, btn9}));
        winningPatterns.add(convertToArrayList(new Button[]{btn1, btn5, btn9}));
        winningPatterns.add(convertToArrayList(new Button[]{btn3, btn5, btn7}));

        player1Path = new ArrayList<Character>();
        player2Path = new ArrayList<>();

        // Needed for score board
        builder = new AlertDialog.Builder(this);
        handler = new Handler();

        // parsing Intent
        player1 = getIntent().getStringExtra(EXTRA_STRING_PLAYER1_NAME);
        player2 = getIntent().getStringExtra(EXTRA_STRING_PLAYER2_NAME);
        boolean playerOneGoFirst = getIntent().getBooleanExtra(EXTRA_BOOLEAN_PLAYER1_GO_FIRST, true);

        if (playerOneGoFirst) {
            currentPlayer = player1;
        } else {
            currentPlayer = player2;
        }

        cleanTheBoard(currentPlayer);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            buttonPressed((Button) view, false);
        }

    }

    private void buttonPressed(Button button, boolean isComputer) {
        String buttonText = button.getText().toString();
        char btnTag = button.getTag().toString().charAt(0);

        Log.d(TAG, "buttonPressed: availableButtons = " + availableButtons);
        Log.d(TAG, "buttonPressed: btnTag = " + String.valueOf(btnTag));

        if (!(buttonText.equalsIgnoreCase("x") || buttonText.equalsIgnoreCase("o"))) {
            if (currentPlayer.equalsIgnoreCase(player1)) {
                if (playerOnePlaysX) {
                    button.setText("X");
                } else {
                    button.setText("O");
                }
                player1Path.add(btnTag);
                availableButtons = availableButtons.replaceFirst(String.valueOf(btnTag), "");
                if (checkForWin(player1Path)) {
                    displayScore(player1);
                    return;
                }
            } else if (currentPlayer.equalsIgnoreCase(player2)) {
                if (playerOnePlaysX) {
                    button.setText("O");
                } else {
                    button.setText("X");
                }
                player2Path.add(btnTag);
                availableButtons = availableButtons.replaceFirst(String.valueOf(btnTag), "");
                if (checkForWin(player2Path)) {
                    displayScore(player2);
                    return;
                }

            } else {
                txtMsgTop.setText("Error occured, please, restart.");
            }

            Log.d(TAG, "onClick: availableButtons = " + availableButtons);
        } else {
            Toast.makeText(getApplicationContext(), "Please, select blank spot!", Toast.LENGTH_SHORT).show();
        }

        currentPlayer = currentPlayer.equals(player1) ? player2 : player1;

        if (!isComputer) {
            checkIfComputerGoNext(currentPlayer);
        }
    }

    private void checkIfComputerGoNext(String playerName) {
        if (availableButtons.isEmpty()) {
            displayScore("");
            Log.w(TAG, "checkIfComputerGoNext: availableButtons is empty");
            return;
        }

        int max = availableButtons.length() - 1;
        int min = 0;
        if (COMPUTER_NAME.toLowerCase().equals(playerName.toLowerCase())) {
            Random random = new Random();
            int randomNum = random.nextInt(max - min + 1) + min;
            Log.d(TAG, "checkIfComputerGoNext: randomNum = " + randomNum);
            final char btnTag = availableButtons.charAt(randomNum);

            // option 1:
            Button btn = btnTable.findViewWithTag(String.valueOf(btnTag));
            buttonPressed(btn, true);

            // option 2:
//            int index = btnTag - '0'; // tag range '1' ~'9'
//            Log.d(TAG, "checkIfComputerGoNext: btnTag = " + String.valueOf(btnTag));
//            Log.d(TAG, "checkIfComputerGoNext: index = " + index);

//            if (index > 0 && index <= btnArray.length) {
//                Button btn = btnArray[index - 1];
//                buttonPressed(btn, true);
//                Log.d(TAG, "checkIfComputerGoNext: btn.getTag() = " + btn.getTag().toString());
//            } else {
//                Log.e(TAG, "checkIfComputerGoNext: index out of range : " + index);
//            }
        }
    }

    private boolean checkForWin(ArrayList<Character> buttons) {
        for (List<Character> pattern : winningPatterns) {
            int length = 0;
            for (int i = 0; i < pattern.size(); i++) {
                if (buttons.contains(pattern.get(i))) {
                    length++;
                }
                if (length == pattern.size()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void displayScore(String winner) {

        // Add points for current winning player
        if (player1.equals(winner))
            p1score++;
        else if (player2.equals(winner))
            p2score++;

        // Display score so-far
        builder.setTitle(txtMsgBottom.getText());
        builder.setMessage(player1 + "'s score = " + p1score + "\n" + player2 + "'s score = " + p2score);

        // Add the buttons
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Clear the game board ...
                cleanTheBoard(player2);
            }
        });
        builder.setNegativeButton("Back to main screen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Update High Score Database with win/losses
                hiScoreDbAdapter.addScoresForPlayer(player1, p1score, gamesPlayed);
                hiScoreDbAdapter.addScoresForPlayer(player2, p2score, gamesPlayed);
                finish(); // Goes back to Welcome screen
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void cleanTheBoard(String nextPlayer) {
        final int white = Color.parseColor("#FAFAFA");
        player1Path.clear();
        player2Path.clear();
        availableButtons = "123456789";
        currentPlayer = nextPlayer;

        if (btnArray == null || btnArray.length == 0) {
            return;
        }
        for (Button btn : btnArray) {
            btn.setText(" ");
            btn.setBackgroundColor(white);
        }
        txtMsgBottom.setText("");
        checkIfComputerGoNext(currentPlayer);
    }
}















