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
                + "_id INTEGER PRIMARY KEY, word TEXT, "
                + "order=DESC);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        throw new RuntimeException("How did we get here?");
    }

    public void insertWords(Context app, List<String> words, int count) {
        SQLiteDatabase db = getDb(app);

        db.beginTransaction();

        db.delete("english_words", null, null);

        try {
            for (String word : words) {
                Object[] args = {count, word};

                db.execSQL("INSERT INTO english_words (_id, word "
                                + ") "
                                + "VALUES (?, ?)",
                        args);
                count++;
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    public Cursor loadWords(Context app, String match) {
        SQLiteDatabase db = getDb(app);

        if (TextUtils.isEmpty(match)) {
            return (db.rawQuery("SELECT * FROM english_words ORDER BY _id ASC LIMIT 0,5",
                    null));
        }

        String[] args = {match + '*'};

        return (db.rawQuery("SELECT * FROM english_words WHERE word "
                + "MATCH ? ORDER BY _id ASC LIMIT 0,5", args));
    }

    private SQLiteDatabase getDb(Context app) {
        if (db == null) {
            db = getInstance(app).getWritableDatabase();
        }

        return (db);
    }
}
