package balittanah.mainpage.ui.recomendationPage;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import balittanah.mainpage.Adapter;
import balittanah.mainpage.Myobj;
import balittanah.mainpage.R;
import balittanah.mainpage.ui.scanPage.scanPageFragment;

public class recomendationPageFragment extends Fragment {
//    private Myobj myobj = new Myobj();
    Button action;
    public TextView value;
    ThisObj thisObj = new ThisObj();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.recomendation_page_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        value = view.findViewById(R.id.value);
        action = view.findViewById(R.id.btnAction);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanPageFragment scanPageFragment = new scanPageFragment();
                value.setText(String.valueOf(scanPageFragment.getA()));
            }
        });
        if (getArguments() != null){
            final double data = getArguments().getDouble("element1");
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value.setText(String.valueOf(data));

                }
            });
        }
    }
    public void RetriveValue(double value1){
        thisObj.setV1(value1);

    }
    public class ThisObj{
        double v1;

        public double getV1() {
            return v1;
        }

        public void setV1(double v1) {
            this.v1 = v1;
        }
    }
}