package com.example.cargostar.view.animation;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AnimUtils {

    public static void hide(final View target, final int start, final int end) {
        final ObjectAnimator minimize = ObjectAnimator.ofInt(target, "top", start, end);
        minimize.setDuration(200);
        minimize.setInterpolator(new LinearInterpolator());
        minimize.start();
    }

    public static void show(final View target, final int start, final int end) {
        final ObjectAnimator maximize = ObjectAnimator.ofInt(target, "top", start, end);
        maximize.setDuration(200);
        maximize.setInterpolator(new LinearInterpolator());
        maximize.start();
    }
}
