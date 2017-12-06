package com.fhda.cs64a.tictactoe;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.fhda.cs64a.tictactoe.GameBoard.EXTRA_BOOLEAN_PLAYER1_GO_FIRST;
import static com.fhda.cs64a.tictactoe.GameBoard.EXTRA_STRING_PLAYER1_NAME;
import static com.fhda.cs64a.tictactoe.GameBoard.EXTRA_STRING_PLAYER2_NAME;


public class OnePlayerActivity extends AppCompatActivity {

    private EditText etFirstPlayerName;
    private TextView firstMovePlayer1;
    private TextView firstMovePlayer2;
    private Button btnStart;
    private Button btnBack;
    AlertDialog alertDialog;

    private boolean isFirstMovePlayer1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);

        etFirstPlayerName = (EditText) findViewById(R.id.etFirstPlayerName);
        firstMovePlayer1 = (TextView) findViewById(R.id.firstMovePlayer1);
        firstMovePlayer2 = (TextView) findViewById(R.id.firstMovePlayer2);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnBack = (Button) findViewById(R.id.btnBack);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Empty Name")
                .setTitle("Error");
        alertDialog = builder.create();

        firstMovePlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    firstMovePlayer1.setBackground(getResources().getDrawable(R.drawable.textview_border));
                    firstMovePlayer2.setBackground(null);
                }
                isFirstMovePlayer1 = true;
            }
        });

        firstMovePlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    firstMovePlayer1.setBackground(null);
                    firstMovePlayer2.setBackground(getResources().getDrawable(R.drawable.textview_border));
                }
                isFirstMovePlayer1 = false;
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstPlayerName = etFirstPlayerName.getText().toString();
                if (firstPlayerName.isEmpty()) {
                    alertDialog.show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), GameBoard.class);
                intent.putExtra(EXTRA_STRING_PLAYER1_NAME, firstPlayerName);
                intent.putExtra(EXTRA_STRING_PLAYER2_NAME, "Android");
                intent.putExtra(EXTRA_BOOLEAN_PLAYER1_GO_FIRST, isFirstMovePlayer1);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
