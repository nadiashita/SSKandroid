package com.si_ware.neospectra.Activities.Interfaces.DataPage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.si_ware.neospectra.Activities.IntroActivity;
import com.si_ware.neospectra.Activities.SettingsActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.R;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;

public class DataPageFragment extends Fragment {
    private Context mContext;
    Button btnOk, btnClose;
    CheckBox checkall, chck1, chck2, chck3, chck4, chck5, chck6, chck7, chck8, chck9, chck10, chck11, chck12, chck13, chck14, chck15, chck16;
    LinearLayout Lay1, Lay2, Lay3, Lay4, Lay5, Lay6, Lay7, Lay8, Lay9, Lay10, Lay11, Lay12, Lay13, Lay14, Lay15, Lay16;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(getActivity(), mContext);
        }
        mContext = getActivity();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);

        Toolbar toolbar = view.findViewById(R.id.titlebar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        Lay1 = view.findViewById(R.id.Lay1);
        Lay2 = view.findViewById(R.id.Lay2);
        Lay3 = view.findViewById(R.id.Lay3);
        Lay4 = view.findViewById(R.id.Lay4);
        Lay5 = view.findViewById(R.id.Lay5);
        Lay6 = view.findViewById(R.id.Lay6);
        Lay7 = view.findViewById(R.id.Lay7);
        Lay8 = view.findViewById(R.id.Lay8);
        Lay9 = view.findViewById(R.id.Lay9);
        Lay10 = view.findViewById(R.id.Lay10);
        Lay11 = view.findViewById(R.id.Lay11);
        Lay12 = view.findViewById(R.id.Lay12);
        Lay13 = view.findViewById(R.id.Lay13);
        Lay14 = view.findViewById(R.id.Lay14);
        Lay15 = view.findViewById(R.id.Lay15);
        Lay16 = view.findViewById(R.id.Lay16);

        chck1 = (CheckBox) view.findViewById(R.id.chck1);
        chck2 = (CheckBox) view.findViewById(R.id.chck2);
        chck3 = (CheckBox) view.findViewById(R.id.chck3);
        chck4 = (CheckBox) view.findViewById(R.id.chck4);
        chck5 = (CheckBox) view.findViewById(R.id.chck5);
        chck6 = (CheckBox) view.findViewById(R.id.chck6);
        chck7 = (CheckBox) view.findViewById(R.id.chck7);
        chck8 = (CheckBox) view.findViewById(R.id.chck8);
        chck9 = (CheckBox) view.findViewById(R.id.chck9);
        chck10 = (CheckBox) view.findViewById(R.id.chck10);
        chck11 = (CheckBox) view.findViewById(R.id.chck11);
        chck12 = (CheckBox) view.findViewById(R.id.chck12);
        chck13 = (CheckBox) view.findViewById(R.id.chck13);
        chck14 = (CheckBox) view.findViewById(R.id.chck14);
        chck15 = (CheckBox) view.findViewById(R.id.chck15);
        chck16 = (CheckBox) view.findViewById(R.id.chck16);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                    .Builder(mContext);
            myAlert.setTitle("Disconnect");
            myAlert.setMessage("Are you sure you want to disconnect the device?");
            myAlert.setPositiveButton("OK", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                // Request of bluetooth disconnection
                if (bluetoothAPI != null) {
                    bluetoothAPI.disconnectFromDevice();

                    Intent iMain = new Intent(mContext, IntroActivity.class);
                    iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(iMain);
                }

            });
            myAlert.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            myAlert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem shareitem = menu.add(Menu.NONE, 101, 0, "Options");
        shareitem.setIcon(R.drawable.icons_setting);
        shareitem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        shareitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final Dialog d = new Dialog(getActivity());
                d.setTitle("SETTING");
                d.setContentView(R.layout.dialog_layoutt);

                //INITIALIZE VIEWS

                checkall = (CheckBox) d.findViewById(R.id.checallornot);
                chck1 = (CheckBox) d.findViewById(R.id.chck1);
                chck2 = (CheckBox) d.findViewById(R.id.chck2);
                chck3 = (CheckBox) d.findViewById(R.id.chck3);
                chck4 = (CheckBox) d.findViewById(R.id.chck4);
                chck5 = (CheckBox) d.findViewById(R.id.chck5);
                chck6 = (CheckBox) d.findViewById(R.id.chck6);
                chck7 = (CheckBox) d.findViewById(R.id.chck7);
                chck8 = (CheckBox) d.findViewById(R.id.chck8);
                chck9 = (CheckBox) d.findViewById(R.id.chck9);
                chck10 = (CheckBox) d.findViewById(R.id.chck10);
                chck11 = (CheckBox) d.findViewById(R.id.chck11);
                chck12 = (CheckBox) d.findViewById(R.id.chck12);
                chck13 = (CheckBox) d.findViewById(R.id.chck13);
                chck14 = (CheckBox) d.findViewById(R.id.chck14);
                chck15 = (CheckBox) d.findViewById(R.id.chck15);
                chck16 = (CheckBox) d.findViewById(R.id.chck16);

                btnOk = (Button) d.findViewById(R.id.btnOk);
                btnClose = (Button) d.findViewById(R.id.btnClose);

                //Login Check all
                checkall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkall.isChecked()) {
                            checkall.setText("Check all");
                            chck1.setChecked(false);
                            chck2.setChecked(false);
                            chck3.setChecked(false);
                            chck4.setChecked(false);
                            chck5.setChecked(false);
                            chck6.setChecked(false);
                            chck7.setChecked(false);
                            chck8.setChecked(false);
                            chck9.setChecked(false);
                            chck10.setChecked(false);
                            chck11.setChecked(false);
                            chck12.setChecked(false);
                            chck13.setChecked(false);
                            chck14.setChecked(false);
                            chck15.setChecked(false);
                            chck16.setChecked(false);
                        }
                        if (checkall.isChecked()) {
                            checkall.setText("Uncheck all");
                            chck1.setChecked(true);
                            chck2.setChecked(true);
                            chck3.setChecked(true);
                            chck4.setChecked(true);
                            chck5.setChecked(true);
                            chck6.setChecked(true);
                            chck7.setChecked(true);
                            chck8.setChecked(true);
                            chck9.setChecked(true);
                            chck10.setChecked(true);
                            chck11.setChecked(true);
                            chck12.setChecked(true);
                            chck13.setChecked(true);
                            chck14.setChecked(true);
                            chck15.setChecked(true);
                            chck16.setChecked(true);
                        }
                    }
                });

                //Action Close
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                //Action Ok
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!chck1.isChecked() ||
                                !chck2.isChecked() ||
                                !chck3.isChecked() ||
                                !chck4.isChecked() ||
                                !chck5.isChecked() ||
                                !chck6.isChecked() ||
                                !chck7.isChecked() ||
                                !chck8.isChecked() ||
                                !chck9.isChecked() ||
                                !chck10.isChecked() ||
                                !chck11.isChecked() ||
                                !chck12.isChecked() ||
                                !chck13.isChecked() ||
                                !chck14.isChecked() ||
                                !chck15.isChecked() ||
                                !chck16.isChecked()) {
                            Toast.makeText(getActivity(), "Please check the list first", Toast.LENGTH_SHORT).show();
                        }
                        if (chck1.isChecked() ||
                                chck2.isChecked() ||
                                chck3.isChecked() ||
                                chck4.isChecked() ||
                                chck5.isChecked() ||
                                chck6.isChecked() ||
                                chck7.isChecked() ||
                                chck8.isChecked() ||
                                chck9.isChecked() ||
                                chck10.isChecked() ||
                                chck11.isChecked() ||
                                chck12.isChecked() ||
                                chck13.isChecked() ||
                                chck14.isChecked() ||
                                chck15.isChecked() ||
                                chck16.isChecked()) {
                            // check box 1
                            if (chck1.isChecked()) {
                                Lay1.setVisibility(View.VISIBLE);
                            } else
                                Lay1.setVisibility(View.GONE);

                            // check box 2
                            if (chck2.isChecked()) {
                                Lay2.setVisibility(View.VISIBLE);
                            } else
                                Lay2.setVisibility(View.GONE);

                            // check box 3
                            if (chck3.isChecked()) {
                                Lay3.setVisibility(View.VISIBLE);
                            } else
                                Lay3.setVisibility(View.GONE);

                            // check box 4
                            if (chck4.isChecked()) {
                                Lay4.setVisibility(View.VISIBLE);
                            } else
                                Lay4.setVisibility(View.GONE);

                            // check box 5
                            if (chck5.isChecked()) {
                                Lay5.setVisibility(View.VISIBLE);
                            } else
                                Lay5.setVisibility(View.GONE);

                            // check box 6
                            if (chck6.isChecked()) {
                                Lay6.setVisibility(View.VISIBLE);
                            } else
                                Lay6.setVisibility(View.GONE);

                            // check box 7
                            if (chck7.isChecked()) {
                                Lay7.setVisibility(View.VISIBLE);
                            } else
                                Lay7.setVisibility(View.GONE);

                            // check box 8
                            if (chck8.isChecked()) {
                                Lay8.setVisibility(View.VISIBLE);
                            } else
                                Lay8.setVisibility(View.GONE);

                            // check box 9
                            if (chck9.isChecked()) {
                                Lay9.setVisibility(View.VISIBLE);
                            } else
                                Lay9.setVisibility(View.GONE);

                            // check box 10
                            if (chck10.isChecked()) {
                                Lay10.setVisibility(View.VISIBLE);
                            } else
                                Lay10.setVisibility(View.GONE);

                            // check box 11
                            if (chck11.isChecked()) {
                                Lay11.setVisibility(View.VISIBLE);
                            } else
                                Lay11.setVisibility(View.GONE);

                            // check box 12
                            if (chck12.isChecked()) {
                                Lay12.setVisibility(View.VISIBLE);
                            } else
                                Lay12.setVisibility(View.GONE);

                            // check box 13
                            if (chck13.isChecked()) {
                                Lay13.setVisibility(View.VISIBLE);
                            } else
                                Lay13.setVisibility(View.GONE);

                            // check box 14
                            if (chck14.isChecked()) {
                                Lay14.setVisibility(View.VISIBLE);
                            } else
                                Lay14.setVisibility(View.GONE);

                            // check box 15
                            if (chck15.isChecked()) {
                                Lay15.setVisibility(View.VISIBLE);
                            } else
                                Lay15.setVisibility(View.GONE);

                            // check box 16
                            if (chck16.isChecked()) {
                                Lay16.setVisibility(View.VISIBLE);
                            } else
                                Lay16.setVisibility(View.GONE);
                            // Display dialog is gone
                            d.dismiss();
                        }

                    }

                });
                //SHOW DIALOG
                d.show();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}