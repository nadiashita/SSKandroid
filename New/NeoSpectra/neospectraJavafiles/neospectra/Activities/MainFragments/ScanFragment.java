package com.si_ware.neospectra.Activities.MainFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbResult;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;
import com.si_ware.neospectra.Scan.View.ScanView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.si_ware.neospectra.DummyClassForTesting.getRandomModule;
import static com.si_ware.neospectra.DummyClassForTesting.listOfModuleNames;
import static com.si_ware.neospectra.Global.Global.CURRENT_MODULE;

public class ScanFragment extends Fragment{

    private View scanFragment;
    private ProgressBar pbScanning;

    private dbResult currentResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 1/3/18 Dummy module for testing
        String moduleName = listOfModuleNames[new Random().nextInt(3)];
        Toast.makeText(getActivity(), moduleName, Toast.LENGTH_SHORT).show();
        CURRENT_MODULE = getRandomModule(getActivity(), moduleName);
        currentResult = new dbResult(CURRENT_MODULE.getModuleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        scanFragment = inflater.inflate(R.layout.fragment_scan, container, false);
        FloatingActionButton btnScan = scanFragment.findViewById(R.id.btnScan);

        pbScanning = scanFragment.findViewById(R.id.pbScanning);

        //List of IDs of progress bars.
        int[] pbIDs = {R.id.pbFats, R.id.pbLactose, R.id.pbProtein, R.id.pbSolids};
        //Create list of four progress bars for testing, will be list view with adapter.
        final List<ProgressBar> bars = new ArrayList<>();
        for (int i: pbIDs){
            bars.add((ProgressBar) scanFragment.findViewById(i));
        }

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            scanFragment.findViewById(R.id.lyResults).setVisibility(View.INVISIBLE);
            pbScanning.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pbScanning.setVisibility(View.INVISIBLE);
                    scanFragment.findViewById(R.id.lyResults).setVisibility(View.VISIBLE);
                    doScan(bars, CURRENT_MODULE);
                }
            }, 3500);
            }
        });
        return scanFragment;
    }

    public void doScan(List<ProgressBar> bars, dbModule currentModule){
        ScanView scanView = new ScanView();
        ScanPresenter scanPresenter = new ScanPresenter();

        currentResult = scanPresenter.scan(currentModule);
        scanView.showResults(getActivity(), currentResult, bars);

    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
}
