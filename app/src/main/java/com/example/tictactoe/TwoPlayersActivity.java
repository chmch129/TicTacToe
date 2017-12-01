package com.example.tictactoe;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.tictactoe.GameBoardActivity.EXTRA_BOOLEAN_PLAYER1_GO_FIRST;
import static com.example.tictactoe.GameBoardActivity.EXTRA_STRING_PLAYER1_NAME;
import static com.example.tictactoe.GameBoardActivity.EXTRA_STRING_PLAYER2_NAME;

public class TwoPlayersActivity extends AppCompatActivity {

    private boolean isFirstMovePlayer1 = true;
    @BindView(R.id.etFirstPlayerName)
    EditText etFirstPlayerName;
    @BindView(R.id.etSecondPlayerName)
    EditText etSecondPlayerName;
    @BindView(R.id.firstMovePlayer1)
    TextView firstMovePlayer1;
    @BindView(R.id.firstMovePlayer2)
    TextView firstMovePlayer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_players);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.firstMovePlayer1)
    void firstMovePlayer1() {
        firstMovePlayer1.setBackground(getResources().getDrawable(R.drawable.textview_border));
        firstMovePlayer2.setBackground(null);
        isFirstMovePlayer1 = true;
    }

    @OnClick(R.id.firstMovePlayer2)
    void firstMovePlayer2() {
        firstMovePlayer1.setBackground(null);
        firstMovePlayer2.setBackground(getResources().getDrawable(R.drawable.textview_border));
        isFirstMovePlayer1 = false;
    }

    @OnClick(R.id.btnStart)
    void btnStartOnClick() {
        String firstPlayerName = etFirstPlayerName.getText().toString();
        String secondPlayerName = etSecondPlayerName.getText().toString();
        if (firstPlayerName.isEmpty() || secondPlayerName.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Empty Name")
                    .setTitle("Error");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        Intent intent = new Intent(this, GameBoardActivity.class);
        intent.putExtra(EXTRA_STRING_PLAYER1_NAME, firstPlayerName);
        intent.putExtra(EXTRA_STRING_PLAYER2_NAME, secondPlayerName);
        intent.putExtra(EXTRA_BOOLEAN_PLAYER1_GO_FIRST, isFirstMovePlayer1);
        startActivity(intent);
    }

    @OnClick(R.id.btnBack)
    void btnBackClick() {
        onBackPressed();
    }
}
