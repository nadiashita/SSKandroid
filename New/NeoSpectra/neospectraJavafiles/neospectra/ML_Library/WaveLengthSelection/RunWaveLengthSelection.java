package com.si_ware.neospectra.ML_Library.WaveLengthSelection;

import android.util.Log;

/**
 * Created by AmrWinter on 1/8/18.
 */

public class RunWaveLengthSelection {

    final public static int SFS = 0;
    final public static int GAPLSSP = 1;

    private static WaveLengthCore core = new WaveLengthCore();
    private static IWaveLengthSelection[] waveLengthMethods = new IWaveLengthSelection[] {
            new IWaveLengthSelection() { public int do_Selection() { return core.SFS(); } },
            new IWaveLengthSelection() { public int do_Selection() { return core.GAPLSSP(); } },
    };

    public static int run(int index) {
        if (index < waveLengthMethods.length){
            return waveLengthMethods[index].do_Selection();
        }
        else{
            Log.e("err_out", "You don't have methods with this index");
            return -1;
        }
    }
}
