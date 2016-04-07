package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class DiagnosisActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diagnosis);

        EditableCardView diagnosis = (EditableCardView) findViewById(R.id.diagnosis);
        diagnosis.setTitle(getString(R.string.diagnosis_title));
        diagnosis.setSubtitle(getString(R.string.diagnosis_subtitle));
        diagnosis.setTitleId(R.string.diagnosis_title);
        diagnosis.setText(preferences.getDiagnosis());
    }
}
