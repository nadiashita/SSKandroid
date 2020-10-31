package com.si_ware.neospectra.Models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AmrWinter on 1/8/18.
 */

public class dbVariableConfig {
    // Variable Configuration
    private String varName = "";
    @NonNull
    private Integer preProcessing_Type = -1;
    @NonNull
    private Integer FeatureExtraction_Type = -1;
    @NonNull
    private ArrayList<Double> Wavelengths = new ArrayList<>();

    public void setPreProcessing_Type(@NonNull Integer preProcessing_Type) {
        this.preProcessing_Type = preProcessing_Type;
    }

    public void setFeatureExtraction_Type(@NonNull Integer featureExtraction_Type) {
        FeatureExtraction_Type = featureExtraction_Type;
    }

    @NonNull
    public ArrayList<Double> getWavelengths() {
        return Wavelengths;
    }

    public void setWavelengths(@NonNull ArrayList<Double> wavelengths) {
        Wavelengths = wavelengths;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public dbVariableConfig(String varName) {
        this.varName = varName;
    }

    public dbVariableConfig(@NonNull Integer preProcessing_Type,
                            @NonNull Integer featureExtraction_Type){
        this.preProcessing_Type = preProcessing_Type;
        FeatureExtraction_Type = featureExtraction_Type;
    }

    public int getPreProcessing_Type() {
        return preProcessing_Type;
    }

    public void setPreProcessing_Type(int preProcessing_Type) {
        this.preProcessing_Type = preProcessing_Type;
    }

    public int getFeatureExtraction_Type() {
        return FeatureExtraction_Type;
    }

    public void setFeatureExtraction_Type(int featureExtraction_Type) {
        FeatureExtraction_Type = featureExtraction_Type;
    }

    @Override
    public String toString() {
        return "\"dbVariableConfig\":{" +
                "\"varName\":\"" + varName + '\"' +
                ", \"preProcessing_Type\":" + preProcessing_Type +
                ", \"FeatureExtraction_Type\":" + FeatureExtraction_Type +
                ", \"Wavelengths\":" + Wavelengths +
                '}';
    }
}
