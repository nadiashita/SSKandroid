package com.si_ware.neospectra.ML_Library.FeatureExtraction;

/**
 * Created by AmrWinter on 1/8/18.
 */

public interface IFeatureExtractionCore {

    int Derivative_1st();
    int Derivative_2nd();
    int Derivatives_cat();
    int Integral();
    int Cat_Original_Integral();
    int Covariance();
    int Hierarchy_of_Covariance();
    int Histograms();
}
