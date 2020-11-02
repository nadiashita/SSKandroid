package com.si_ware.neospectra.dbtable;


//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.si_ware.neospectra.R;

import java.util.Calendar;
import java.util.TimeZone;

public class addUpdateRecord extends AppCompatActivity {

    private EditText edtBray, edtCa, edtClay, edtCn, edtHclk2o, edtHclP2o5, edtJumlah, edtK, edtKbAdj, edtKjeldahl, edtKtk,
            edtMg, edtMorgan, edtNa, edtOlsen, edtPhH2o, edtPhKcl, edtretensip, edtSand, edtSilt, edtWbc;
    private FloatingActionButton saveBtn;

    private String bray, ca, clay, cn, hclk2o, hclp2o5, jumlah, k, kbadj, kjelhal, ktk, mg, morgan, na, olsen, phh2o, phkcl, retensip,
            sand, silt, wbc;

    //dbHelper
    private DBHelper dbHelper;

    //action bar

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_record);


        //init
        actionBar = getSupportActionBar();
//

        edtBray = findViewById(R.id.edtBray);
        //title actionn bar
//        actionBar.setTitle("Add Record");
//        //back button
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        edtCa = findViewById(R.id.edtCa);
        edtClay = findViewById(R.id.edtClay);
        edtCn = findViewById(R.id.edtCn);
        edtHclk2o = findViewById(R.id.edtHclK2o);
        edtHclP2o5 = findViewById(R.id.edtHclP2o5);
        edtJumlah = findViewById(R.id.edtJumlah);
        edtK = findViewById(R.id.edtK);
        edtKbAdj = findViewById(R.id.edtKbAdj);
        edtKjeldahl= findViewById(R.id.edtkjeldahl);
        edtKtk = findViewById(R.id.edtKtk);
        edtMg = findViewById(R.id.edtMg);
        edtMorgan = findViewById(R.id.edtMorgan);
        edtNa = findViewById(R.id.edtNa);
        edtOlsen = findViewById(R.id.edtOlsen);
        edtPhH2o = findViewById(R.id.edtPhH2o);
        edtPhKcl = findViewById(R.id.edtPhKcl);
        edtretensip = findViewById(R.id.edtretensip);
        edtSand = findViewById(R.id.edtSand);
        edtSilt = findViewById(R.id.edtSilt);
        edtWbc = findViewById(R.id.edtWbc);

        saveBtn = findViewById(R.id.saveBtn);
        //init db helper
        dbHelper = new DBHelper(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputData();

            }
        });


    }

    private void inputData() {
        // get data
        bray = "" + edtBray.getText().toString().trim();
        ca = "" + edtCa.getText().toString().trim();
        clay = "" + edtClay.getText().toString().trim();
        cn = "" + edtCn.getText().toString().trim();
        hclk2o = "" + edtHclk2o.getText().toString().trim();
        hclp2o5 = "" + edtHclP2o5.getText().toString().trim();
        jumlah = "" + edtJumlah.getText().toString().trim();
        k = "" + edtK.getText().toString().trim();
        kbadj = "" + edtKbAdj.getText().toString().trim();
        kjelhal = "" + edtKjeldahl.getText().toString().trim();
        ktk = "" + edtKtk.getText().toString().trim();
        mg = "" + edtMg.getText().toString().trim();
        morgan = "" + edtHclk2o.getText().toString().trim();
        na = "" + edtMorgan.getText().toString().trim();
        olsen = "" + edtOlsen.getText().toString().trim();
        phh2o = "" + edtPhH2o.getText().toString().trim();
        phkcl = "" + edtPhKcl.getText().toString().trim();
        retensip = "" + edtretensip.getText().toString().trim();
        sand = "" + edtSand.getText().toString().trim();
        silt = "" + edtSilt.getText().toString().trim();
        wbc = "" + edtWbc.getText().toString().trim();





        //save to db
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int tahun = calendar.get(Calendar.YEAR);
        int bulan = calendar.get(Calendar.MONTH) + 1;
        int tanggal = calendar.get(Calendar.DAY_OF_MONTH);
        String timestamp = "" + tahun + "-" + bulan + "-" + tanggal;


        long id = dbHelper.insertRecord(
                "" + bray,
                "" + ca,
                "" + clay,
                "" + cn,
                "" + hclk2o,
                "" + hclp2o5,
                "" + jumlah,
                "" + k,
                "" + kbadj,
                "" + kjelhal,
                "" + ktk,
                "" + mg,
                "" + morgan,
                "" + na,
                "" + olsen,
                "" + phh2o,
                "" + cn,"" + phkcl,
                "" + sand,
                "" + silt,
                "" + wbc,

                "" + timestamp


        );

        Toast.makeText(this, "Record Added againts ID:" + id, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}