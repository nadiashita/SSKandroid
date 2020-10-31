package com.si_ware.neospectra.Models;

/**
 * Created by AmrWinter on 12/28/17.
 */

public class dbVariable {
    private String varName;
    private dbVarMetaData varMetaData;
    private dbVariableConfig variableConfig;
    private dbDataMatrix variableData;

    public dbVariable(String varName){
        this.varName = varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public dbVarMetaData getVarMetaData() {
        return varMetaData;
    }

    public void setVarMetaData(dbVarMetaData varMetaData) {
        this.varMetaData = varMetaData;
    }

    public dbVariableConfig getVariableConfig() {
        return variableConfig;
    }

    public void setVariableConfig(dbVariableConfig variableConfig) {
        this.variableConfig = variableConfig;
    }

    public String getVarName() {
        return varName;
    }

    public dbDataMatrix getVariableData() {
        return variableData;
    }

    public void setVariableData(dbDataMatrix variableData) {
        this.variableData = variableData;
    }

    @Override
    public String toString() {
        return "\"dbVariable\"{" +
                "\"varName\":\"" + varName + '\"' +
                ", \"varMetaData\":" + varMetaData +
                ", \"variableConfig\":" + variableConfig +
                ", \"variableData\":" + variableData +
                '}';
    }
}
