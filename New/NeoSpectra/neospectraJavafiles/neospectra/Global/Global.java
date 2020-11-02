package com.si_ware.neospectra.Global;

import com.si_ware.neospectra.Models.dbModule;

/**
 * Created by AmrWinter on 12/30/17.
 */

public class Global {
    public static final String API_KEY = "AIzaSyDIGB7vtglG3b_N1E7FsmFaTI29K3xOp4E";
    public static final String CLIENT_ID = "273224885324-a554g3hd7331k6ggbtc3got0gqdpp3fg.apps.googleusercontent.com";

    public static final String KEY_MODULE_NAME = "MODULE_NAME";
    public static final String KEY_MODULE_ID = "MODULE_ID";
    public static final String KEY_MODULE_DATA = "MODULE_DATA";
    public static final String KEY_ERROR_NOT_FOUND = "Not Found";
    public static final String FILE_NAME_RESULTS = "_resultsFile";



    // TODO: 1/3/18 Change this line to get the current module in the future.
    public static dbModule CURRENT_MODULE;// = dbModule.getModuleInstance();
    public static final int TYPE_MODULE_REGRESSION = 1;
    public static final int TYPE_MODULE_CLASSIFICATION = 2;
}
