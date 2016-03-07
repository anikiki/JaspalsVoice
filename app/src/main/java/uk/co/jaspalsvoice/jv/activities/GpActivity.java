package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.custom.MedicalContactCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class GpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gp);

        MedicalContactCardView gp = (MedicalContactCardView) findViewById(R.id.gp_name);
        gp.setTitle(getString(R.string.gp_title));
        gp.setLabel1View(getString(R.string.gp_medical_team_name));
        gp.setLabel2View(getString(R.string.gp_medical_team_contact_details));

        MedicalContactCardView mndContact = (MedicalContactCardView) findViewById(R.id.mnd_contact);
        mndContact.setTitle(getString(R.string.mnd_contact_title));
        mndContact.setLabel1View(getString(R.string.gp_medical_team_name));
        mndContact.setLabel2View(getString(R.string.gp_medical_team_contact_details));

        MedicalContactCardView physiotherapist = (MedicalContactCardView) findViewById(R.id.physiotherapist);
        physiotherapist.setTitle(getString(R.string.physiotherapist_title));
        physiotherapist.setLabel1View(getString(R.string.gp_medical_team_name));
        physiotherapist.setLabel2View(getString(R.string.gp_medical_team_contact_details));

        MedicalContactCardView therapist = (MedicalContactCardView) findViewById(R.id.therapist);
        therapist.setTitle(getString(R.string.therapist_title));
        therapist.setLabel1View(getString(R.string.gp_medical_team_name));
        therapist.setLabel2View(getString(R.string.gp_medical_team_contact_details));

        MedicalContactCardView carer = (MedicalContactCardView) findViewById(R.id.carer);
        carer.setTitle(getString(R.string.carer_title));
        carer.setLabel1View(getString(R.string.gp_medical_team_name));
        carer.setLabel2View(getString(R.string.gp_medical_team_contact_details));

        MedicalContactCardView other = (MedicalContactCardView) findViewById(R.id.other_medical);
        other.setTitle(getString(R.string.other_medical_title));
        other.setLabel1View(getString(R.string.gp_medical_team_name));
        other.setLabel2View(getString(R.string.gp_medical_team_contact_details));
    }
}
