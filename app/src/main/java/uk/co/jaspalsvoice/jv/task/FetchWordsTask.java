package uk.co.jaspalsvoice.jv.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

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
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                StringBuilder results = new StringBuilder(150);
                do {
                    Log.i(TAG, "onPostExecute: " + cursor.getString(1));
                    results.append(cursor.getString(1));
                    results.append(" ");
                } while (cursor.moveToNext());
                if (results.length() > 0) {
                    onResultsListener.onUpdateUi(results.toString());
                    return;
                }
            }
        }
        onResultsListener.onUpdateUi(null);
    }

    public interface OnResultsListener {
        void onUpdateUi(String text);
    }
}
