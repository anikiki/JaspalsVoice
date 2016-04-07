package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.YesNoCardView;
import uk.co.jaspalsvoice.jv.views.tickbox.TickBoxListCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class AboutMeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_me);

        TickBoxListCardView sitting = (TickBoxListCardView) findViewById(R.id.sitting_position);
        sitting.setTitle(getString(R.string.about_me_sitting_position));

        TickBoxListCardView needToUse = (TickBoxListCardView) findViewById(R.id.need_to_use);
        needToUse.setTitle(getString(R.string.about_me_need_to_use));

        YesNoCardView breathing = (YesNoCardView) findViewById(R.id.breathing);
        breathing.setTitle(getString(R.string.about_me_breathing));

        TickBoxListCardView breathingWhen = (TickBoxListCardView) findViewById(R.id.breathing_when);
        breathingWhen.setTitle(getString(R.string.about_me_breathing_when));

        TickBoxListCardView sleepPosition = (TickBoxListCardView) findViewById(R.id.sleep_position);
        sleepPosition.setTitle(getString(R.string.about_me_sleep_position));

        YesNoCardView swallowingDifficulties = (YesNoCardView) findViewById(R.id.swallowing_difficulties);
        swallowingDifficulties.setTitle(getString(R.string.about_me_swallowing_difficulties));
        swallowingDifficulties.setSubtitle(getString(R.string.about_me_swallowing_difficulties_subtitle));

        YesNoCardView physicalAbilities = (YesNoCardView) findViewById(R.id.physical_abilities);
        physicalAbilities.setTitle(getString(R.string.about_me_physical_abilities));
        physicalAbilities.setSubtitle(getString(R.string.about_me_physical_abilities_subtitle));

        TickBoxListCardView transferTo = (TickBoxListCardView) findViewById(R.id.transfer_to);
        transferTo.setTitle(getString(R.string.about_me_transfer_to));

    }
}
