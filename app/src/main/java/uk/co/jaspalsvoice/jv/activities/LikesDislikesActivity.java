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

        EditableCardView likes = (EditableCardView) findViewById(R.id.likes);
        likes.setTitle(getString(R.string.likes));
        likes.setText(getString(R.string.likes));

        EditableCardView dislikes = (EditableCardView) findViewById(R.id.dislikes);
        dislikes.setTitle(getString(R.string.dislikes));
        dislikes.setText(getString(R.string.dislikes));
    }
}
