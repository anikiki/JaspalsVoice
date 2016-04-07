package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class MedicalAllergiesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medical_allergies);

        EditableCardView medicalAllergies = (EditableCardView) findViewById(R.id.medical_allergies);
        medicalAllergies.setTitle(getString(R.string.medical_allergies_title));
        medicalAllergies.setTitleId(R.string.medical_allergies_title);
        medicalAllergies.setText(preferences.getMedicalAllergies());
    }
}
