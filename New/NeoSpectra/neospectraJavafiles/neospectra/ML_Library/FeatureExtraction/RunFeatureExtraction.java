package com.si_ware.neospectra.ML_Library.FeatureExtraction;

import android.util.Log;

/**
 * Created by AmrWinter on 1/8/18.
 */

public class RunFeatureExtraction {

    public static final int TYPE_FeatureExtraction_Derivative_1st = 0;
    public static final int TYPE_FeatureExtraction_Derivative_2nd = 1;
    public static final int TYPE_FeatureExtraction_Derivatives_cat = 2;
    public static final int TYPE_FeatureExtraction_Integral = 3;
    public static final int TYPE_FeatureExtraction_Cat_Original_Integral = 4;
    public static final int TYPE_FeatureExtraction_Covariance = 5;
    public static final int TYPE_FeatureExtraction_Hierarchy_of_Covariance = 6;
    public static final int TYPE_FeatureExtraction_Histograms = 7;

    private static FeatureExtractionCore core = new FeatureExtractionCore();
    private static IFeatureExtraction[] FeatureExtractionMethods = new IFeatureExtraction[] {
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Derivative_1st(); } },
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Derivative_2nd(); } },
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Derivatives_cat(); } },
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Integral(); } },
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Cat_Original_Integral(); } },
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Covariance(); } },
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Hierarchy_of_Covariance(); } },
            new IFeatureExtraction() { public int do_FeatureExtraction() { return core.Histograms(); } },
    };

    public static int run(int index) {
        if (index < FeatureExtractionMethods.length){
            return FeatureExtractionMethods[index].do_FeatureExtraction();
        }
        else{
            Log.e("err_out", "You don't have methods with this index");
            return -1;
        }
    }
}
