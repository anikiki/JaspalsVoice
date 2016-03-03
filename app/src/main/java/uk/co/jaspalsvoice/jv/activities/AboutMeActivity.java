package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;
import uk.co.jaspalsvoice.jv.views.TickBoxListCardView;
import uk.co.jaspalsvoice.jv.views.YesNoCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_me);

        TickBoxListCardView communicate = (TickBoxListCardView) findViewById(R.id.sitting_position);
        communicate.setTitle(getString(R.string.about_me_sitting_position));

        YesNoCardView seeingHearing = (YesNoCardView) findViewById(R.id.breathing);
        seeingHearing.setTitle(getString(R.string.about_me_breathing));

        EditableCardView eat = (EditableCardView) findViewById(R.id.eat);
        eat.setTitle(getString(R.string.about_me_eat));
        eat.setText(getString(R.string.about_me_eat));

        EditableCardView drink = (EditableCardView) findViewById(R.id.drink);
        drink.setTitle(getString(R.string.about_me_drink));
        drink.setText(getString(R.string.about_me_drink));

        EditableCardView toilet = (EditableCardView) findViewById(R.id.toilet);
        toilet.setTitle(getString(R.string.about_me_toilet));
        toilet.setText(getString(R.string.about_me_toilet));

        EditableCardView sleep = (EditableCardView) findViewById(R.id.sleep);
        sleep.setTitle(getString(R.string.about_me_sleep));
        sleep.setText(getString(R.string.about_me_sleep));

        EditableCardView personalCare = (EditableCardView) findViewById(R.id.care);
        personalCare.setTitle(getString(R.string.about_me_care));
        personalCare.setText(getString(R.string.about_me_care));
    }
}
