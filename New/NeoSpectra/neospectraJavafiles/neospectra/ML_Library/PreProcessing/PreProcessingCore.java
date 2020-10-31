package com.si_ware.neospectra.ML_Library.PreProcessing;

import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_EMSC;
import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_OPLEC;
import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_OSC1;
import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_OSC2;

/**
 * Created by AmrWinter on 1/8/18.
 */

public class PreProcessingCore implements IPreProcessingCore {

    @Override
    public int osc_1() {
        return TYPE_PreProcessing_OSC1;
    }

    @Override
    public int osc_2() {
        return TYPE_PreProcessing_OSC2;
    }

    @Override
    public int emsc() {
        return TYPE_PreProcessing_EMSC;
    }

    @Override
    public int oplec() {
        return TYPE_PreProcessing_OPLEC;
    }
}
