package com.si_ware.neospectra.ML_Library.FeatureExtraction;

import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Cat_Original_Integral;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Covariance;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Derivative_1st;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Derivative_2nd;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Derivatives_cat;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Hierarchy_of_Covariance;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Histograms;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Integral;

/**
 * Created by AmrWinter on 1/8/18.
 */

public class FeatureExtractionCore implements IFeatureExtractionCore {
    @Override
    public int Derivative_1st() {
        return TYPE_FeatureExtraction_Derivative_1st;
    }

    @Override
    public int Derivative_2nd() {
        return TYPE_FeatureExtraction_Derivative_2nd;
    }

    @Override
    public int Derivatives_cat() {
        return TYPE_FeatureExtraction_Derivatives_cat;
    }

    @Override
    public int Integral() {
        return TYPE_FeatureExtraction_Integral;
    }

    @Override
    public int Cat_Original_Integral() {
        return TYPE_FeatureExtraction_Cat_Original_Integral;
    }

    @Override
    public int Covariance() {
        return TYPE_FeatureExtraction_Covariance;
    }

    @Override
    public int Hierarchy_of_Covariance() {
        return TYPE_FeatureExtraction_Hierarchy_of_Covariance;
    }

    @Override
    public int Histograms() {
        return TYPE_FeatureExtraction_Histograms;
    }
}
