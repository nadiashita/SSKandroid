package com.si_ware.neospectra.ML_Library.Module;

import android.content.Context;

import com.si_ware.neospectra.Models.dbModuleMetaData;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Models.dbResult;

/**
 * Created by AmrWinter on 1/7/18.
 */

public interface IModule {
    dbResult run(dbReading reading);
}
