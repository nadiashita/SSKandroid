package com.si_ware.neospectra.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by AmrWinter on 1/13/18.
 */

public class dbModuleConfig {
    private dbModuleMetaData metaData;
    private ArrayList<dbVariableConfig> variablesConfig;
    @NonNull
    private Integer Module_Type = -1;

    @NonNull
    public Integer getModule_Type() {
        return Module_Type;
    }

    public void setModule_Type(@NonNull Integer module_Type) {
        Module_Type = module_Type;
    }

    public dbModuleConfig(dbModuleMetaData metaData) {
        this.metaData = metaData;
    }

    public ArrayList<dbVariableConfig> getVariablesConfig() {
        return variablesConfig;
    }

    public void setVariablesConfig(ArrayList<dbVariableConfig> variablesConfig) {
        this.variablesConfig = variablesConfig;
    }

    @Override
    public String toString() {
        return "dbModuleConfig{" +
                "metaData=" + metaData +
                ", variablesConfig=" + variablesConfig +
                ", Module_Type=" + Module_Type +
                '}';
    }
}
