package com.si_ware.neospectra.Models;

/**
 * Created by AmrWinter on 1/12/18.
 */

public class dbSettings {
    private String primaryModuleName = "Milk";
    private boolean saveAllResults = false;

    public String getPrimaryModuleName() {
        return primaryModuleName;
    }

    public void setPrimaryModuleName(String primaryModuleName) {
        this.primaryModuleName = primaryModuleName;
    }

    public boolean isSaveAllResults() {
        return saveAllResults;
    }

    public void setSaveAllResults(boolean saveAllResults) {
        this.saveAllResults = saveAllResults;
    }

    @Override
    public String toString() {
        return "dbSettings{" +
                "primaryModuleName='" + primaryModuleName + '\'' +
                ", saveAllResults=" + saveAllResults +
                '}';
    }
}
