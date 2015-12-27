package uk.co.jaspalsvoice.jv.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Ana on 12/21/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "english_words.db";
    private static final int SCHEMA = 1;
    private static volatile DatabaseHelper SINGLETON = null;
    private SQLiteDatabase db = null;

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (SINGLETON == null) {
            SINGLETON = new DatabaseHelper(context);
        }
        return (SINGLETON);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE VIRTUAL TABLE english_words USING fts4("
                + "word TEXT, rank INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        throw new RuntimeException("How did we get here?");
    }

    public void insertWords(Context app, List<String> words) {
        SQLiteDatabase db = getDb(app);
        db.beginTransaction();
        db.delete("english_words", null, null);
        try {
            for (String word : words) {
                Object[] args = {word, 0};
                db.execSQL("INSERT INTO english_words (word, rank) VALUES (?, ?)", args);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void insertWord(Context app, String word) {
        SQLiteDatabase db = getDb(app);
        db.beginTransaction();
        try {
            String[] arg = {word};
            Cursor cursor = db.rawQuery("SELECT * FROM english_words WHERE word MATCH ? LIMIT 0,1", arg);
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    Object[] args = {word, 0};
                    db.execSQL("INSERT INTO english_words (word, rank) VALUES (?, ?)", args);
                } else {
                    if (cursor.moveToFirst()) {
                        int rank = cursor.getInt(1);
                        rank += 1;
                        db.execSQL("UPDATE english_words SET rank = '" + rank + "' WHERE word = '" + word + "';");
                    }
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor loadWords(Context app, String match) {
        SQLiteDatabase db = getDb(app);
        if (TextUtils.isEmpty(match)) {
            return (db.rawQuery("SELECT * FROM english_words ORDER BY rank DESC LIMIT 0,5", null));
        }
        String[] args = {match + '*'};
        return (db.rawQuery("SELECT * FROM english_words WHERE word "
                + "MATCH ? ORDER BY rank DESC LIMIT 0,5", args));
    }

    private SQLiteDatabase getDb(Context app) {
        if (db == null) {
            db = getInstance(app).getWritableDatabase();
        }
        return (db);
    }
}
