package com.si_ware.neospectra.ML_Library.PreProcessing;

import android.util.Log;

/**
 * Created by AmrWinter on 1/8/18.
 */

public class RunPreProcessing {

    public static final int TYPE_PreProcessing_OSC1 = 0;
    public static final int TYPE_PreProcessing_OSC2 = 1;
    public static final int TYPE_PreProcessing_EMSC = 2;
    public static final int TYPE_PreProcessing_OPLEC = 3;

    private static PreProcessingCore core = new PreProcessingCore();
    private static IPreProcessing[] preProcessingMethods = new IPreProcessing[] {
            new IPreProcessing() { public int do_PreProcessing() { return core.osc_1(); } },
            new IPreProcessing() { public int do_PreProcessing() { return core.osc_2(); } },
            new IPreProcessing() { public int do_PreProcessing() { return core.emsc(); } },
            new IPreProcessing() { public int do_PreProcessing() { return core.oplec(); } },
    };

    public static int run(int index) {
        if (index < preProcessingMethods.length){
            return preProcessingMethods[index].do_PreProcessing();
        }
        else{
            Log.e("err_out", "You don't have methods with this index");
            return -1;
        }
    }
}
