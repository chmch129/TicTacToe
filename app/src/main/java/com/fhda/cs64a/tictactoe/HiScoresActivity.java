package com.fhda.cs64a.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Displays the contents of the high score database.
 */

public class HiScoresActivity extends AppCompatActivity {
    private HiScoreDbAdapter highScoreDbAdapter;
    private TextView tvScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hi_scores);
        tvScores = (TextView) findViewById(R.id.tvScores);

        // Open a handle to the scores database
        highScoreDbAdapter = new HiScoreDbAdapter(this);

        if (highScoreDbAdapter.hasData()) {
            String[] highScores = highScoreDbAdapter.getHiScoresAsStringArray();
            for (int i = 0; i < highScores.length; i++) {
                tvScores.append(System.getProperty("line.separator"));
                tvScores.append(highScores[i]);
            }
        }
        else {
            tvScores.setText("No high scores yet.");
        }

    }

    public void onDismiss (View view) {
        finish();
    }

    public void onClickDelete (View view) {
        highScoreDbAdapter.deleteAll();
        tvScores.setText("Deleted All High Scores.");
    }
}
