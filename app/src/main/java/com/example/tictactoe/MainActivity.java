package com.example.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnOnePlayer)
    void btnOnePlayerOnClick() {
        openActivity(OnePlayerActivity.class);
    }

    @OnClick(R.id.btnTwoPlayers)
    void btnTwoPlayersOnClick() {
        openActivity(TwoPlayersActivity.class);
    }

    @OnClick(R.id.btnGameRules)
    void btnGameRulesOnClick() {
        openActivity(GameRulesActivity.class);
    }

    @OnClick(R.id.btnExit)
    void btnExitOnClick() {
        finish();
    }

    public void openActivity(Class<?> pClass) {
        Intent intent = new Intent(this, pClass);
        startActivity(intent);
    }

}
