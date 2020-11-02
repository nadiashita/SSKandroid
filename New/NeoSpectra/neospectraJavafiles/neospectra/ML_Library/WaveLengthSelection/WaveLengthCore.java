package com.si_ware.neospectra.ML_Library.WaveLengthSelection;

import static com.si_ware.neospectra.ML_Library.WaveLengthSelection.RunWaveLengthSelection.GAPLSSP;
import static com.si_ware.neospectra.ML_Library.WaveLengthSelection.RunWaveLengthSelection.SFS;

/**
 * Created by AmrWinter on 1/9/18.
 */

public class WaveLengthCore implements IWaveLengthCore {
    @Override
    public int GAPLSSP() {
        return GAPLSSP;
    }

    @Override
    public int SFS() {
        return SFS;
    }
}
