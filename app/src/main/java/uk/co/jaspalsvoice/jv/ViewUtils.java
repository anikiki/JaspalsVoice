package uk.co.jaspalsvoice.jv;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ana on 2/7/2016.
 */
public class ViewUtils {
    private final static String TAG = ViewUtils.class.getSimpleName();

    public static void flipViews(final View viewToScaleDown, final View viewToScaleUp) {
        if (viewToScaleDown == null || viewToScaleUp == null) {
            Log.e(TAG, "Attempting to make changes on a null view.");
            return;
        }

        Context context = viewToScaleDown.getContext();
        int duration = context.getResources().getInteger(android.R.integer.config_shortAnimTime) / 2;

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(viewToScaleDown, "scaleX", 1.0f, 0.0f);
        scaleDownX.setDuration(duration);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(viewToScaleUp, "scaleX", 0.0f, 1.0f);
        scaleUpX.setDuration(duration);

        scaleDownX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewToScaleDown.setVisibility(View.INVISIBLE);
                viewToScaleDown.setScaleX(1.0f);
                viewToScaleUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet flip = new AnimatorSet();
        flip.playSequentially(scaleDownX, scaleUpX);

        flip.start();
    }

    public static void flipTextView(final TextView view, final String firstText, final String secondText) {
        if (view == null) {
            Log.e(TAG, "Attempting to make changes on a null view.");
            return;
        }

        Context context = view.getContext();
        int duration = context.getResources().getInteger(android.R.integer.config_shortAnimTime) / 2;

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.0f);
        scaleDownX.setDuration(duration);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.0f);
        scaleUpX.setDuration(duration);

        scaleDownX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(1.0f);
                if (view.getText().toString().equals(firstText)) {
                    view.setText(secondText);
                } else {
                    view.setText(firstText);
                }
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet flip = new AnimatorSet();
        flip.playSequentially(scaleDownX, scaleUpX);

        flip.start();
    }
}
