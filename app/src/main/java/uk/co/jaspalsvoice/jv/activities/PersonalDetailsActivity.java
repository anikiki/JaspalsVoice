package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;
import uk.co.jaspalsvoice.jv.views.YesNoCardView;

/**
 * Created by Ana on 2/7/2016.
 */
public class PersonalDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_details);

        EditableCardView name = (EditableCardView) findViewById(R.id.pd_name);
        name.setTitle(getString(R.string.personal_details_name));

        EditableCardView nickname = (EditableCardView) findViewById(R.id.pd_name_to_be_called);
        nickname.setTitle(getString(R.string.personal_details_name_to_be_called));

        EditableCardView liveWith = (EditableCardView) findViewById(R.id.pd_live_with);
        liveWith.setTitle(getString(R.string.personal_details_live_with));

        EditableCardView email = (EditableCardView) findViewById(R.id.pd_email);
        email.setTitle(getString(R.string.personal_details_email));

        EditableCardView dob = (EditableCardView) findViewById(R.id.pd_dob);
        dob.setTitle(getString(R.string.personal_details_dob));

        EditableCardView mainCarer = (EditableCardView) findViewById(R.id.pd_main_carer);
        mainCarer.setTitle(getString(R.string.personal_details_main_carer));

        EditableCardView carerTel = (EditableCardView) findViewById(R.id.pd_carer_tel);
        carerTel.setTitle(getString(R.string.personal_details_carer_tel));

        YesNoCardView translatorNeeded = (YesNoCardView) findViewById(R.id.pd_translator_needed);
        translatorNeeded.setTitle(getString(R.string.personal_details_translator_needed));
    }
}
