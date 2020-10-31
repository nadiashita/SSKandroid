package com.si_ware.neospectra.Models;

/**
 * Created by AmrWinter on 1/7/18.
 */

public class dbVarMetaData {
    private String varName;
    private double varNormalValue, varLowerRange, varUpperRange;

    public dbVarMetaData(String varName) {
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public double getVarNormalValue() {
        return varNormalValue;
    }

    public void setVarNormalValue(double varNormalValue) {
        this.varNormalValue = varNormalValue;
    }

    public double getVarLowerRange() {
        return varLowerRange;
    }

    public void setVarLowerRange(double varLowerRange) {
        this.varLowerRange = varLowerRange;
    }

    public double getVarUpperRange() {
        return varUpperRange;
    }

    public void setVarUpperRange(double varUpperRange) {
        this.varUpperRange = varUpperRange;
    }

    @Override
    public String toString() {
        return "\"dbVarMetaData\":\"{" +
                "\"varName\":\"" + varName + '\"' +
                ", \"varNormalValue\":" + varNormalValue +
                ", \"varLowerRange\":" + varLowerRange +
                ", \"varUpperRange\":" + varUpperRange +
                '}';
    }
}
