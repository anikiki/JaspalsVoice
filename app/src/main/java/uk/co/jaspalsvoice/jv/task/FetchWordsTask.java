package uk.co.jaspalsvoice.jv.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.db.DatabaseHelper;

/**
 * Created by Ana on 12/21/2015.
 */
public class FetchWordsTask extends AsyncTask<String, Void, Cursor> {
    private static final String TAG = FetchWordsTask.class.getSimpleName();

    private Context appContext;
    private OnResultsListener onResultsListener;

    public FetchWordsTask(Context appContext, OnResultsListener onResultsListener) {
        this.appContext = appContext;
        this.onResultsListener = onResultsListener;
    }

    @Override
    protected Cursor doInBackground(String... params) {
        try {
            return DatabaseHelper.getInstance(appContext).loadWords(appContext, params[0]);
        } catch (Exception e) {
            Log.e(TAG, "Exception populating database", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        Log.i(TAG, "onPostExecute: Words found: ");
        List<String> results = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Log.i(TAG, "onPostExecute: " + cursor.getString(0));
                    results.add(cursor.getString(0));
                } while (cursor.moveToNext());
                if (results.size() > 0) {
                    onResultsListener.onUpdateUi(results);
                    return;
                }
            }
        }
        onResultsListener.onUpdateUi(results);
    }

    public interface OnResultsListener {
        void onUpdateUi(List<String> text);
    }
}
