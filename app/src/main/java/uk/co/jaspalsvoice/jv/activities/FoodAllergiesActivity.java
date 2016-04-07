package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class FoodAllergiesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_allergies);

        EditableCardView foodAllergies = (EditableCardView) findViewById(R.id.food_allergies);
        foodAllergies.setTitle(getString(R.string.food_allergies_title));
        foodAllergies.setTitleId(R.string.food_allergies_title);
        foodAllergies.setText(preferences.getFoodAllergies());
    }
}
