package com.fhda.cs64a.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * High score database for tic-tac-toe game
 */

public class HiScoreDbAdapter {
    private SQLiteDatabase db;
    private HiScoreDb dbHelper;

    public static final String HISCORE_DBNAME = "HiScores";
    public static final int HISCORE_DBVERSION = 1;
    public static final String HISCORE_TABLE = "scores";

    // Column names and indexes for high score table
    public static final String KEY_ID = "_id";
    public static final String KEY_PLAYER = "player";
    public static final String KEY_WINS = "wins";
    public static final String KEY_GAMES = "games";
    private static final String[] ALL_KEYS={KEY_ID,KEY_PLAYER,KEY_WINS,KEY_GAMES};
    //protected static final int COL_KEY_ID=0;
    protected static final int COL_KEY_PLAYER=1;
    protected static final int COL_KEY_WINS=2;
    protected static final int COL_KEY_GAMES=3;


    public HiScoreDbAdapter(Context context) {
        dbHelper = new HiScoreDb(context);
    }

    public int getHiscoreDbversion() {
        return HISCORE_DBVERSION;
    }

    public boolean hasData () {
        // Let caller know if db is empty or not
        int rowCount;
        open();
        Cursor data = getHiScores();
        rowCount = data.getCount();
        close();

        if (rowCount > 0)
            return true;

        return false;
    }

    public HiScoreDbAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public Cursor getHiScores () {
        open();
        Cursor cursor = db.query(true, HISCORE_TABLE,ALL_KEYS, null, null, null, null,
                KEY_WINS+" DESC, "+KEY_GAMES+" ASC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        close();
        return cursor;
    }

    public String[] getHiScoresAsStringArray() {
        Cursor cursor = getHiScores();
        int scoreCount = cursor.getCount();
        String[] allScores = new String[scoreCount];

        if (scoreCount > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < scoreCount; i++) {
                allScores[i] = String.format("%d. %s\t(%d wins, %d losses)", (i + 1),
                        cursor.getString(COL_KEY_PLAYER),
                        cursor.getInt(COL_KEY_WINS),
                        (cursor.getInt(COL_KEY_GAMES) - cursor.getInt(COL_KEY_WINS)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return (allScores);

    }
    public long addScoresForPlayer (String name, int wins, int games) {
        long row_id = -1;
        ContentValues values = new ContentValues();

        if (wins <= games) {
            // Cannot win more games than played
            String whereString = (KEY_PLAYER+"=?");
            String[] whereArgs = {name};
            open();
            Cursor cursor = db.query(true, HISCORE_TABLE, ALL_KEYS, whereString, whereArgs,
                    null, null, null, null);
            if (cursor.getCount() > 0) {
                // Existing player has new scores to be added
                cursor.moveToFirst();
                int newWins  = wins + cursor.getInt(COL_KEY_WINS);
                int newGames = games + cursor.getInt(COL_KEY_GAMES);
                values.put(KEY_WINS, newWins);
                values.put(KEY_GAMES, newGames);
                db.update(HISCORE_TABLE, values, whereString, whereArgs);
            } else {
                // New player in db
                values.put(KEY_PLAYER, name);
                values.put(KEY_WINS, wins);
                values.put(KEY_GAMES, games);
                row_id = db.insert(HISCORE_TABLE, null, values);
            }
            close();
        }
        return row_id;

    }

    public void deleteAll() {
        String whereString = KEY_ID+"=";
        Cursor cursor = getHiScores();
        open();
        int rowid = cursor.getColumnIndexOrThrow(KEY_ID);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                db.delete(HISCORE_TABLE, whereString+cursor.getLong(rowid), null);
            } while (cursor.moveToNext());
        }
        close();
    }

    public void close() {
        dbHelper.close();
    }

    public class HiScoreDb extends SQLiteOpenHelper {


        public HiScoreDb(Context context) {
            super(context, HISCORE_DBNAME, null, HISCORE_DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createSql = "CREATE TABLE " + HISCORE_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_PLAYER + " TEXT, " + KEY_WINS + " INTEGER, " +
                    KEY_GAMES + " INTEGER);";
            db.execSQL(createSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HISCORE_TABLE + ";");
            onCreate(db);
        }
    }
}
