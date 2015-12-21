package uk.co.jaspalsvoice.jv.task;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.db.DatabaseHelper;

/**
 * Created by Ana on 12/21/2015.
 */
public class InitWordsTask extends AsyncTask<Object, Void, Void> {
    private static final String TAG = InitWordsTask.class.getSimpleName();

    private Context appContext;

    public InitWordsTask(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    protected Void doInBackground(Object... params) {
        Cursor cursor = DatabaseHelper.getInstance(appContext).loadWords(appContext, "eng");
        if (cursor != null && cursor.getCount() > 0) {
            return null;
        }
        List<String> words = new ArrayList<>();
        try {
            Resources res = (Resources) params[0];
            InputStream inputStream = res.openRawResource(R.raw.words);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                words.add(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception opening dictionary file.", e);
        }
        try {
            DatabaseHelper.getInstance(appContext).insertWords(appContext, words, 0);
        } catch (Exception e) {
            Log.e(TAG, "Exception populating database", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.i(TAG, "onPostExecute: Dictionary loaded");
    }
}
