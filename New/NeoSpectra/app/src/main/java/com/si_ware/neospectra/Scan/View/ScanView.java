package com.si_ware.neospectra.Scan.View;

import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.si_ware.neospectra.myViews.ProgressBarAnimation;

/**
 *
 */

public class ScanView {


    public void animateResult(@NonNull ProgressBar progressBar, int duration, String varName, float result) {
        ProgressBarAnimation anim;
        anim = new ProgressBarAnimation(progressBar, 0, result);
        anim.setDuration(duration);
        progressBar.startAnimation(anim);
    }

}
