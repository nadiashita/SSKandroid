package com.si_ware.neospectra.Models;

import java.util.ArrayList;

/**
 * Created by AmrWinter on 12/18/2017.
 */

public class dbModule {
    private String moduleName;
    private ArrayList<dbVariable> moduleVariables;
    private int moduleType = -1;

    public dbModule(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ArrayList<dbVariable> getModuleVariables() {
        return moduleVariables;
    }

    public void setModuleVariables(ArrayList<dbVariable> moduleVariables) {
        this.moduleVariables = moduleVariables;
    }

    public void clearModule(){
        moduleName = "";
        moduleVariables = null;
        moduleType = -1;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public String toString() {
        return "{\"dbModule\":{" +
                "\"moduleName\":\"" + moduleName + '\"' +
                ", \"moduleVariables\":" + moduleVariables +
                ", \"moduleType\":" + moduleType +
                "}}";
    }
}
