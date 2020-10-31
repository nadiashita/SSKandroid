package com.si_ware.neospectra.Global;

import android.content.Context;
import android.content.DialogInterface;

import com.si_ware.neospectra.Models.dbResult;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.IScanPresenter;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

/**
 * Created by AmrWinter on 1/7/18.
 */

public class Factory {
    public static void showAlertMessage(Context mContext, String title, String message){
        android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                .Builder(mContext);
        myAlert.setTitle(title);
        myAlert.setMessage(message);
        myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myAlert.show();
    }

}
