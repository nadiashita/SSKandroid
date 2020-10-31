package com.si_ware.neospectra.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by AmrWinter on 12/26/2017.
 */

public class dbResult {

    // TODO: 1/7/18 Apply the concept of Interfaces the results.
    // TODO: 1/7/18 Will have, IResult, RegressionResult and ClassificationResult.
    // Types of results:  1.Regression, 2.Classification.

    private String moduleName, timestamp;
    private ArrayList<dbVarResult> results = new ArrayList<>();

    public dbResult(String moduleName){
        this.moduleName = moduleName;
        SimpleDateFormat s = new SimpleDateFormat("hh:mm - dd,MM,yyyy");
        this.timestamp = s.format(new Date());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getModuleName() {
        return moduleName;
    }

    public ArrayList<dbVarResult> getResults() {
        return results;
    }

    public void addVariableResult(String varName, double resultValue){
        dbVarResult tmpVar = new dbVarResult(varName, resultValue);
        this.results.add(tmpVar);
    }

    public void setResults(ArrayList<dbVarResult> results) {
        this.results = results;
    }

    public class dbVarResult {
        private String varName = "";
        private Double varResult = 0.0;

        public dbVarResult(String name, Double result){
            this.varName = name;
            this.varResult = result;
        }

        public String getVarName() {
            return varName;
        }

        public void setVarName(String varName) {
            this.varName = varName;
        }

        public Double getVarResult() {
            return varResult;
        }

        public void setVarResult(Double varResult) {
            this.varResult = varResult;
        }

        @Override
        public String toString() {
            return "dbVarResult{" +
                    "varName=\"" + varName + '\'' +
                    ", varResult=" + varResult +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "dbResult{" +
                "moduleName=\"" + moduleName + '\'' +
                ", timestamp=\"" + timestamp + '\'' +
                ", results=" + results +
                '}';
    }
}
