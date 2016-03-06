package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.EditableCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class LikesDislikesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_likes_dislikes);

        EditableCardView dailyRoutine = (EditableCardView) findViewById(R.id.daily_routine);
        dailyRoutine.setTitle(getString(R.string.likes_dislikes_daily_routine));

        EditableCardView hobbies = (EditableCardView) findViewById(R.id.hobbies);
        hobbies.setTitle(getString(R.string.likes_dislikes_hobbies));

        EditableCardView music = (EditableCardView) findViewById(R.id.music);
        music.setTitle(getString(R.string.likes_dislikes_music));

        EditableCardView tv = (EditableCardView) findViewById(R.id.tv);
        tv.setTitle(getString(R.string.likes_dislikes_tv));

        EditableCardView other = (EditableCardView) findViewById(R.id.other);
        other.setTitle(getString(R.string.likes_dislikes_other));
    }
}
