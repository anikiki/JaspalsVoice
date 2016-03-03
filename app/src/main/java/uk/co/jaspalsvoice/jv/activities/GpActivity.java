package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class GpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gp);

        EditableCardView name = (EditableCardView) findViewById(R.id.gp_name);
        name.setTitle(getString(R.string.gp_name));
        name.setText(getString(R.string.gp_name));

        EditableCardView address = (EditableCardView) findViewById(R.id.gp_address);
        address.setTitle(getString(R.string.gp_address));
        address.setText(getString(R.string.gp_address));

        EditableCardView phone = (EditableCardView) findViewById(R.id.gp_phone);
        phone.setTitle(getString(R.string.gp_phone));
        phone.setText(getString(R.string.gp_phone));
    }
}
