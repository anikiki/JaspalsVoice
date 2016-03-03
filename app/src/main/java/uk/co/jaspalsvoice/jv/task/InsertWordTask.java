package uk.co.jaspalsvoice.jv.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import uk.co.jaspalsvoice.jv.db.DatabaseHelper;

/**
 * Created by Ana on 12/21/2015.
 */
public class InsertWordTask extends AsyncTask<String, Void, String> {
    private static final String TAG = InsertWordTask.class.getSimpleName();

    private WeakReference<Context> context;

    public InsertWordTask(WeakReference<Context> context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Context ctx = context.get();
            if (ctx != null) {
                DatabaseHelper.getInstance(ctx).insertWord(ctx, params[0]);
            }
            return params[0];
        } catch (Exception e) {
            Log.e(TAG, "Exception populating database", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String v) {
        Context ctx = context.get();
        if (ctx != null) {
            if (v != null) {
                Toast.makeText(ctx, "'" + v + "' was added to dictionary.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onPostExecute: Word inserted");
            } else {
                Toast.makeText(ctx, "Error saving word", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onPostExecute: Word not inserted");
            }
        }
    }
}
