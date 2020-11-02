package com.si_ware.neospectra.Scan.View;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ProgressBar;

import com.si_ware.neospectra.Models.dbResult;
import com.si_ware.neospectra.Models.dbVariable;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.IScanPresenter;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;
import com.si_ware.neospectra.myViews.ProgressBarAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AmrWinter on 1/9/18.
 */

public class ScanView implements IScanView {
    @Override
    public void showResults(Context mContext, dbResult result, List<ProgressBar> resultBars) {
        if (result.getResults().size() == resultBars.size()){
            for (int i = 0; i < resultBars.size(); i++){
                animateResult(
                        //provide the current progressbar
                        resultBars.get(i),
                        //set duration
                        4000 + i * 300,
                        //Variable name
                        result.getResults().get(i).getVarName(),
                        //Get the result and cast it from double to float
                        (result.getResults().get(i).getVarResult().floatValue()));
            }
            askToSaveResults(mContext, result);
        }else {
            Log.e("err_NotMatching", mContext.getString(R.string.err_sizes_not_matching));
        }
    }

    @Override
    public void askToSaveResults(final Context mContext, final dbResult result){
        String title = mContext.getString(R.string.title_save_results);
        String body = mContext.getString(R.string.ask_to_save_results);
        android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                .Builder(mContext);
        myAlert.setTitle(title);
        myAlert.setMessage(body);
        myAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IScanPresenter presenter = new ScanPresenter();
                presenter.saveResult(mContext, result);
                dialogInterface.dismiss();
            }
        });
        myAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        myAlert.show();
    }

    @Override
    public void animateResult(ProgressBar progressBar, int duration, String varName, float result) {
        ProgressBarAnimation anim;
        anim = new ProgressBarAnimation(progressBar, 0, result);
        anim.setDuration(duration);
        progressBar.startAnimation(anim);
    }

}
