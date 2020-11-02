package com.si_ware.neospectra.Activities.MainFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si_ware.neospectra.R;

/**
 * Created by AmrWinter on 12/23/2017.
 */

public class AboutFragment extends Fragment {

    View myFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_about_us, container, false);

        return myFragment;
    }
}
