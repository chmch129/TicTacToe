package com.fhda.cs64a.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnOnePlayer;
    private Button btnTwoPlayers;
    private Button btnGameRules;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOnePlayer = (Button) findViewById(R.id.btnOnePlayer);
        btnTwoPlayers = (Button) findViewById(R.id.btnTwoPlayers);
        btnGameRules = (Button) findViewById(R.id.btnGameRules);
        btnExit = (Button) findViewById(R.id.btnExit);

        btnOnePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(OnePlayerActivity.class);
            }
        });
        btnTwoPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(TwoPlayersActivity.class);
            }
        });
        btnGameRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(GameRulesActivity.class);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void openActivity(Class<?> pClass) {
        Intent intent = new Intent(this, pClass);
        startActivity(intent);
    }

}
