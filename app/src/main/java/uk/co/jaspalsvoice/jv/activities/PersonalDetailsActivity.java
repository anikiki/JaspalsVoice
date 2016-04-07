package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;
import uk.co.jaspalsvoice.jv.views.YesNoCardView;

/**
 * Created by Ana on 2/7/2016.
 */
public class PersonalDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_details);

        EditableCardView name = (EditableCardView) findViewById(R.id.pd_name);
        name.setTitle(getString(R.string.personal_details_name));
        name.setTitleId(R.string.personal_details_name);
        name.setText(preferences.getPersonalDetailsName());

        EditableCardView nickname = (EditableCardView) findViewById(R.id.pd_name_to_be_called);
        nickname.setTitle(getString(R.string.personal_details_name_to_be_called));
        nickname.setTitleId(R.string.personal_details_name_to_be_called);
        nickname.setText(preferences.getPersonalDetailsPreferredName());

        EditableCardView liveWith = (EditableCardView) findViewById(R.id.pd_live_with);
        liveWith.setTitle(getString(R.string.personal_details_live_with));
        liveWith.setTitleId(R.string.personal_details_live_with);
        liveWith.setText(preferences.getPersonalDetailsLiveWith());

        EditableCardView email = (EditableCardView) findViewById(R.id.pd_email);
        email.setTitle(getString(R.string.personal_details_email));
        email.setTitleId(R.string.personal_details_email);
        email.setText(preferences.getPersonalDetailsEmail());

        EditableCardView dob = (EditableCardView) findViewById(R.id.pd_dob);
        dob.setTitle(getString(R.string.personal_details_dob));
        dob.setTitleId(R.string.personal_details_dob);
        dob.setText(preferences.getPersonalDetailsDateOfBirth());

        EditableCardView mainCarer = (EditableCardView) findViewById(R.id.pd_main_carer);
        mainCarer.setTitle(getString(R.string.personal_details_main_carer));
        mainCarer.setTitleId(R.string.personal_details_main_carer);
        mainCarer.setText(preferences.getPersonalDetailsMainCarer());

        EditableCardView carerTel = (EditableCardView) findViewById(R.id.pd_carer_tel);
        carerTel.setTitle(getString(R.string.personal_details_carer_tel));
        carerTel.setTitleId(R.string.personal_details_carer_tel);
        carerTel.setText(preferences.getPersonalDetailsCarerTel());

        YesNoCardView translatorNeeded = (YesNoCardView) findViewById(R.id.pd_translator_needed);
        translatorNeeded.setTitle(getString(R.string.personal_details_translator_needed));
        translatorNeeded.setTitleId(R.string.personal_details_translator_needed);
        String[] answers = getResources().getStringArray(R.array.yes_no_spinner_item);
        translatorNeeded.setText(preferences.getPersonalDetailsNeedTranslator() ? answers[1] : answers[0]);
    }
}
