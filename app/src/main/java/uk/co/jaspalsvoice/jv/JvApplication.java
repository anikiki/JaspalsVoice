package uk.co.jaspalsvoice.jv;

import android.app.Application;

import uk.co.jaspalsvoice.jv.db.DbHelper;
import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Ana on 3/21/2016.
 */
public class JvApplication extends Application {

    private DbHelper dbHelper;
    private JvPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DbHelper(new DbOpenHelper(this));
        preferences = new JvPreferences(this);
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public JvPreferences getPreferences() {
        return preferences;
    }

}