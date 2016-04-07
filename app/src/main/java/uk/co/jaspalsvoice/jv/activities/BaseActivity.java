package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.JvPreferences;

/**
 * Created by Ana on 4/7/2016.
 */
public class BaseActivity extends AppCompatActivity {

    private JvApplication application;
    protected JvPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        application = (JvApplication) getApplicationContext();
        preferences = application.getPreferences();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
