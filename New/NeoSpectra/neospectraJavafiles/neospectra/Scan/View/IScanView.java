package com.si_ware.neospectra.Scan.View;

import android.content.Context;
import android.widget.ProgressBar;

import com.si_ware.neospectra.Models.dbResult;

import java.util.List;

/**
 * Created by AmrWinter on 1/9/18.
 */

public interface IScanView {
    void animateResult(ProgressBar progressBar, int duration, String varName, float result);
    void showResults(Context mContext, dbResult result, List<ProgressBar> resultBars);
    void askToSaveResults(Context mContext, dbResult result);

}
