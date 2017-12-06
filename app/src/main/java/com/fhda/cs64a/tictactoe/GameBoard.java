package com.fhda.cs64a.tictactoe;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameBoard extends AppCompatActivity  {

    public final static String EXTRA_STRING_PLAYER1_NAME = "EXTRA_STRING_PLAYER1_NAME";
    public final static String EXTRA_STRING_PLAYER2_NAME = "EXTRA_STRING_PLAYER2_NAME";
    public final static String EXTRA_BOOLEAN_PLAYER1_GO_FIRST = "EXTRA_BOOLEAN_PLAYER1_GO_FIRST";

    public String player1;
    public String player2;
    public boolean playerOnePlaysX;
    public boolean computerPlays;
    private String currentPlayer;
    private HiScoreDbAdapter hiScoreDbAdapter;

    private TextView txtMsgTop;
    private TextView txtMsgBottom;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private View.OnClickListener btnView;

    private AlertDialog.Builder builder;
    private int p1score, p2score, gamesPlayed;
    private int turnNumber = -1; //-1 because first initial check for win is not a real turn, but initializer for players and text fields

    private void displayScore (String winner) {

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
                int white = Color.parseColor("#FAFAFA");
                btn1.setText(" "); btn1.setBackgroundColor(white);
                btn2.setText(" "); btn2.setBackgroundColor(white);
                btn3.setText(" "); btn3.setBackgroundColor(white);
                btn4.setText(" "); btn4.setBackgroundColor(white);
                btn5.setText(" "); btn5.setBackgroundColor(white);
                btn6.setText(" "); btn6.setBackgroundColor(white);
                btn7.setText(" "); btn7.setBackgroundColor(white);
                btn8.setText(" "); btn8.setBackgroundColor(white);
                btn9.setText(" "); btn9.setBackgroundColor(white);
                txtMsgBottom.setText("");
                turnNumber = -1; //-1 because first initial check for win is not a real turn, but initializer for players and text fields
                currentPlayer = player2;
                checkForWin();
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 1500);



    }
    private void checkForWin(){
        String winningCombo;
        // Keep track if game is over (tie)
        turnNumber++;

        if(currentPlayer.equalsIgnoreCase(player1)){
            if(playerOnePlaysX){
                winningCombo = "XXX";
            }
            else{
                winningCombo = "OOO";
            }
        }
        else if(currentPlayer.equalsIgnoreCase(player2)){
            if(playerOnePlaysX){
                winningCombo = "OOO";
            }
            else{
                winningCombo = "XXX";
            }
        }
        else{

            txtMsgTop.setText("Error occured, please, restart.");
            winningCombo = "ERROR";
        }
        HashMap<Integer, Button[]> winningPattern = new HashMap<Integer, Button[]>();
        winningPattern.put(0,new Button[]{btn1,btn2,btn3});
        winningPattern.put(1,new Button[]{btn4,btn5,btn6});
        winningPattern.put(2,new Button[]{btn7,btn8,btn9});
        winningPattern.put(3,new Button[]{btn1,btn4,btn7});
        winningPattern.put(4,new Button[]{btn2,btn5,btn8});
        winningPattern.put(5,new Button[]{btn3,btn6,btn9});
        winningPattern.put(6,new Button[]{btn1,btn5,btn9});
        winningPattern.put(7,new Button[]{btn3,btn5,btn7});

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
        for(int i = 0; i<8;i++ ){
            if(winningCombo.equalsIgnoreCase(possibleCombinations.get(i).toString())){
                winCombo = i;
                break;
            }

        }

        if(winCombo == 9) {
            // This happens if no winner.
            if (turnNumber == 9) {
                txtMsgBottom.setText("Tie Game!");
                this.displayScore(null);
                return; // we need return here if, because if we play with computer and it is tie, computer will attempt to do its turn with error.
            }
            System.out.println(turnNumber);
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
                txtMsgTop.setText(player2 + "'s turn (x)");
            } else if (computerPlays && playerOnePlaysX && currentPlayer.equalsIgnoreCase(player1)) {
                txtMsgTop.setText(player1 + "'s turn (x)");
            } else if (computerPlays && !playerOnePlaysX && currentPlayer.equalsIgnoreCase(player1)) {
                txtMsgTop.setText(player1 + "'s turn (o)");
            } else if (computerPlays && playerOnePlaysX && currentPlayer.equalsIgnoreCase(player2)){
                txtMsgTop.setText(player2 + "'s turn (o)");
                computerPlays();
            }else if (computerPlays && !playerOnePlaysX && currentPlayer.equalsIgnoreCase(player2)){
                txtMsgTop.setText(player2 + "'s turn (x)");
                computerPlays();
            }

            else {
                txtMsgTop.setText("Error occured, please, restart.");
            }
        }
        else{
            // Keep track of games played and won (*not* tie games)
            gamesPlayed++;

            txtMsgBottom.setText(currentPlayer + " won!");

            Button[] flashButtons =  winningPattern.get(winCombo);

            for(int x =2;x>=0; x--){
                flashButtons[x].setBackgroundColor(Color.parseColor("#17c762"));
            }

            this.displayScore(currentPlayer);
        }

    };

    public void computerPlays(){
        String computerPlaysLetter = "";
        String player1PlaysLetter ="";
        if(playerOnePlaysX){
            computerPlaysLetter = "O";
            player1PlaysLetter = "X";
        }
        else{
            computerPlaysLetter = "X";
            player1PlaysLetter = "O";
        }

        HashMap<Integer, Button[]> winningPattern = new HashMap<Integer, Button[]>();
        winningPattern.put(0,new Button[]{btn1,btn2,btn3});
        winningPattern.put(1,new Button[]{btn4,btn5,btn6});
        winningPattern.put(2,new Button[]{btn7,btn8,btn9});
        winningPattern.put(3,new Button[]{btn1,btn4,btn7});
        winningPattern.put(4,new Button[]{btn2,btn5,btn8});
        winningPattern.put(5,new Button[]{btn3,btn6,btn9});
        winningPattern.put(6,new Button[]{btn1,btn5,btn9});
        winningPattern.put(7,new Button[]{btn3,btn5,btn7});

        ArrayList possibleCombinations = new ArrayList<String>();
        possibleCombinations.add(btn1.getText().toString() + btn2.getText().toString() + btn3.getText().toString());
        possibleCombinations.add(btn4.getText().toString() + btn5.getText().toString() + btn6.getText().toString());
        possibleCombinations.add(btn7.getText().toString() + btn8.getText().toString() + btn9.getText().toString());
        possibleCombinations.add(btn1.getText().toString() + btn4.getText().toString() + btn7.getText().toString());
        possibleCombinations.add(btn2.getText().toString() + btn5.getText().toString() + btn8.getText().toString());
        possibleCombinations.add(btn3.getText().toString() + btn6.getText().toString() + btn9.getText().toString());
        possibleCombinations.add(btn1.getText().toString() + btn5.getText().toString() + btn9.getText().toString());
        possibleCombinations.add(btn3.getText().toString() + btn5.getText().toString() + btn7.getText().toString());

        // Search for player 1 two consecutive letters in winning combos
        //And try to prevent player 1 from winning
        int decision = 8;
        for(int i = 0; i<8;i++ ){
            if(possibleCombinations.get(i).toString().contains(player1PlaysLetter+player1PlaysLetter)){
                decision = i;
                break;
            }
        }


        if(decision < 8){
            if(possibleCombinations.get(decision).toString().equalsIgnoreCase(player1PlaysLetter + player1PlaysLetter+" ")){
                Button[] buttons =  winningPattern.get(decision);
                buttons[2].setText(computerPlaysLetter);
                checkForWin();
                return;

            }
            if(possibleCombinations.get(decision).toString().equalsIgnoreCase(player1PlaysLetter + " " +player1PlaysLetter)){
                Button[] buttons =  winningPattern.get(decision);
                buttons[1].setText(computerPlaysLetter);
                checkForWin();
                return;
            }
            if(possibleCombinations.get(decision).toString().equalsIgnoreCase(" " + player1PlaysLetter+player1PlaysLetter)){
                Button[] buttons =  winningPattern.get(decision);
                buttons[0].setText(computerPlaysLetter);
                checkForWin();
                return;
            }

        }
        // Search for computer two consecutive letters in winning combos
        //And try to win by filling it with third letter
        decision = 8;
        for(int i = 0; i<8;i++ ){
            if(possibleCombinations.get(i).toString().contains(computerPlaysLetter+computerPlaysLetter)){
                decision = i;
                break;
            }
        }

        if(decision < 8){
            if(possibleCombinations.get(decision).toString().equalsIgnoreCase(computerPlaysLetter + computerPlaysLetter+" ")){
                Button[] buttons =  winningPattern.get(decision);
                buttons[2].setText(computerPlaysLetter);
                checkForWin();
                return;

            }
            if(possibleCombinations.get(decision).toString().equalsIgnoreCase(computerPlaysLetter + " " +computerPlaysLetter)){
                Button[] buttons =  winningPattern.get(decision);
                buttons[1].setText(computerPlaysLetter);
                checkForWin();
                return;
            }
            if(possibleCombinations.get(decision).toString().equalsIgnoreCase(" " + computerPlaysLetter+computerPlaysLetter)){
                Button[] buttons =  winningPattern.get(decision);
                buttons[0].setText(computerPlaysLetter);
                checkForWin();
                return;
            }

        }
        // Search for at least one computer letters in winning combos
        //And try to extend winning combo by adding another letter
        decision = 8;
        ArrayList<Integer> combinations = new ArrayList<>();
        for(int i = 0; i<8;i++ ){
            if(possibleCombinations.get(i).toString().contains(computerPlaysLetter)){
                combinations.add(i);
            }
        }
        if(!combinations.isEmpty()){

            for (int i = combinations.size(); i>=0;i--){
                Button[] buttons =  winningPattern.get(i);
                if(buttons[0].getText().toString().equalsIgnoreCase(computerPlaysLetter) && buttons[1].getText().toString().equalsIgnoreCase(" ")){
                    buttons[1].setText(computerPlaysLetter);
                    checkForWin();
                    return;
                }
                if(buttons[0].getText().toString().equalsIgnoreCase(computerPlaysLetter) && buttons[2].getText().toString().equalsIgnoreCase(" ")){
                    buttons[2].setText(computerPlaysLetter);
                    checkForWin();
                    return;
                }
                if(buttons[1].getText().toString().equalsIgnoreCase(computerPlaysLetter) && buttons[0].getText().toString().equalsIgnoreCase(" ")){
                    buttons[0].setText(computerPlaysLetter);
                    checkForWin();
                    return;
                }
                if(buttons[1].getText().toString().equalsIgnoreCase(computerPlaysLetter) && buttons[2].getText().toString().equalsIgnoreCase(" ")){
                    buttons[2].setText(computerPlaysLetter);
                    checkForWin();
                    return;
                }
                if(buttons[2].getText().toString().equalsIgnoreCase(computerPlaysLetter) && buttons[1].getText().toString().equalsIgnoreCase(" ")){
                    buttons[1].setText(computerPlaysLetter);
                    checkForWin();
                    return;
                }
                if(buttons[2].getText().toString().equalsIgnoreCase(computerPlaysLetter) && buttons[0].getText().toString().equalsIgnoreCase(" ")){
                    buttons[0].setText(computerPlaysLetter);
                    checkForWin();
                    return;
                }
            }

        }
        else{
            // If gamepad has zero computer letter, lets try to put it in the bes center position
            if(btn5.getText().toString().equalsIgnoreCase(" ")) {
                btn5.setText(computerPlaysLetter);
                checkForWin();
                return;
            }
            else{
                // If gamepad has zero computer letter, and center position is occupied, lets pick random position
                ArrayList<Button> buttonsArray = new ArrayList<>();
                buttonsArray.add(btn1);
                buttonsArray.add(btn2);
                buttonsArray.add(btn3);
                buttonsArray.add(btn4);
                buttonsArray.add(btn5);
                buttonsArray.add(btn6);
                buttonsArray.add(btn7);
                buttonsArray.add(btn8);
                buttonsArray.add(btn9);

                while(true){
                    Random ran = new Random();
                    int x = ran.nextInt(9);
                    if(buttonsArray.get(x).getText().toString().equalsIgnoreCase(" ")) {
                        buttonsArray.get(x).setText(computerPlaysLetter);
                        checkForWin();
                        return;
                    }
                }



            }
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        // Create High Score database handle
        hiScoreDbAdapter = new HiScoreDbAdapter(this);
        txtMsgTop = (TextView) findViewById(R.id.txtMsgTop);
        txtMsgBottom = (TextView) findViewById(R.id.txtMsgBottom);
        btn1  = (Button) findViewById(R.id.btn1);
        btn1.setText(" ");
        btn2  = (Button) findViewById(R.id.btn2);
        btn2.setText(" ");
        btn3  = (Button) findViewById(R.id.btn3);
        btn3.setText(" ");
        btn4  = (Button) findViewById(R.id.btn4);
        btn4.setText(" ");
        btn5  = (Button) findViewById(R.id.btn5);
        btn5.setText(" ");
        btn6  = (Button) findViewById(R.id.btn6);
        btn6.setText(" ");
        btn7  = (Button) findViewById(R.id.btn7);
        btn7.setText(" ");
        btn8  = (Button) findViewById(R.id.btn8);
        btn8.setText(" ");
        btn9  = (Button) findViewById(R.id.btn9);
        btn9.setText(" ");

        //This is data needed from welcome screen.
        player1 = "Steven";
        player2 = "Maya";
        playerOnePlaysX = false;
        computerPlays = true;

        //This is data needed from welcome screen.

        currentPlayer = player2;

        if(computerPlays){
            player2 = "Computer";
        }

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

        btnView = new View.OnClickListener(){
            public void onClick(View v){

                Button b = (Button)v;
                String buttonText = b.getText().toString();
                if (!(buttonText.equalsIgnoreCase("x") || buttonText.equalsIgnoreCase("o"))){

                    if(currentPlayer.equalsIgnoreCase(player1) && playerOnePlaysX){
                        b.setText("X");
                    }
                    else if (currentPlayer.equalsIgnoreCase(player1) && !playerOnePlaysX){
                        b.setText("O");
                    }
                    else if (currentPlayer.equalsIgnoreCase(player2) && playerOnePlaysX){
                        b.setText("O");
                    }
                    else if (currentPlayer.equalsIgnoreCase(player2) && !playerOnePlaysX){
                        b.setText("X");
                    }
                    else{
                        txtMsgTop.setText("Error occured, please, restart.");
                    }

                    checkForWin();
                }

                else{
                    Toast.makeText(getApplicationContext(),"Please, select blank spot!", Toast.LENGTH_SHORT).show();
                }

            }
        };
        btn1.setOnClickListener(btnView);
        btn2.setOnClickListener(btnView);
        btn3.setOnClickListener(btnView);
        btn4.setOnClickListener(btnView);
        btn5.setOnClickListener(btnView);
        btn6.setOnClickListener(btnView);
        btn7.setOnClickListener(btnView);
        btn8.setOnClickListener(btnView);
        btn9.setOnClickListener(btnView);
}



    //Testing git push
    //Testing git update project -- erin
}
