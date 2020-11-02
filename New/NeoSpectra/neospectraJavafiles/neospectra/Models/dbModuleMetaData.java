package com.si_ware.neospectra.Models;

import java.util.List;

/**
 * Created by AmrWinter on 1/7/18.
 */

public class dbModuleMetaData {

    private String moduleName;
    private List<dbVarMetaData> metaVariables;

    public dbModuleMetaData(String moduleName, List<dbVarMetaData> metaVariables) {
        this.moduleName = moduleName;
        this.metaVariables = metaVariables;
    }

    public dbModuleMetaData(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<dbVarMetaData> getMetaVariables() {
        return metaVariables;
    }

    public void setMetaVariables(List<dbVarMetaData> metaVariables) {
        this.metaVariables = metaVariables;
    }

    @Override
    public String toString() {
        return "dbModuleMetaData{" +
                "moduleName='" + moduleName + '\'' +
                ", metaVariables=" + metaVariables +
                '}';
    }
}
