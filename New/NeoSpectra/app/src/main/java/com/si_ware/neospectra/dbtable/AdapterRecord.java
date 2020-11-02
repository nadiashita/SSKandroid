package com.si_ware.neospectra.dbtable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.si_ware.neospectra.Activities.Interfaces.ExportPage.ExportPageFragment;
import com.si_ware.neospectra.Activities.MainPage;
import com.si_ware.neospectra.R;

import java.util.ArrayList;

public class AdapterRecord extends RecyclerView.Adapter<AdapterRecord.HolderRecord> {

    //variables
    private Context context;
    private ArrayList<ModelRecord> recordList;

    //DB helper
    private DBHelper dbHelper;


    //constructor
    public AdapterRecord(Context context, ArrayList<ModelRecord> recordList) {
        this.context = context ;
        this.recordList = recordList;

        dbHelper = new DBHelper(context);
    }





    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //infate Layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_record, parent, false);

        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderRecord holder, int position) {
        //get data, set data, handel view clics in this method

        //get data
        ModelRecord model = recordList.get(position);
        final String id = model.getId();
        String bray = model.getBray();
        String ca = model.getCa();
        String clay = model.getClay();
        String cn = model.getCn();
        String hclk2o = model.getHclk2o();
        String hclp2o5 = model.getHclp2o5();
        String jumlah = model.getJumlah();
        String k = model.getK();
        String kbAdj = model.getKbadj();
        String kjeldah = model.getKjeldahl();
        String ktk = model.getKtk();
        String mg = model.getMg();
        String morgan = model.getMorgan();
        String na = model.getNa();
        String olsen = model.getOlsen();
        String phh2o = model.getPhh2o();
        String phkcl = model.getPhkcl();
        String retensip = model.getRetensip();
        String sand = model.getSand();
        String silt = model.getSilt();
        String wbc = model.getWbc();

        String addedTime = model.getAddedTime();


        //set data to view
        holder.txtId.setText(id);
        holder.txtBray.setText(bray);
        holder.txtCa.setText(ca);
        holder.txtClay.setText(clay);
        holder.txtCn.setText(cn);
        holder.txtHclK2o.setText(hclk2o);
        holder.txtHclP2o5.setText(hclp2o5);
        holder.txtJumlah.setText(jumlah);
        holder.txtK.setText(k);
        holder.txtKbAdj.setText(kbAdj);
        holder.txtKjeldahl.setText(kjeldah);
        holder.txtKtk.setText(ktk);
        holder.txtMg.setText(mg);
        holder.txtMorgan.setText(morgan);
        holder.txtNa.setText(na);
        holder.txtOlsen.setText(olsen);
        holder.txtPhh20.setText(phh2o);
        holder.txtPhkcl.setText(phkcl);
        holder.txtRetensip.setText(retensip);
        holder.txtSand.setText(sand);
        holder.txtSilt.setText(silt);
        holder.txtWbc.setText(wbc);

        //handle item click (delete
        // )
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete data with id = " + id + " ? ");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dbHelper.deleteData(id);
//                        ((ExportPageFragment)context).onResume(); //error code saat recode dari activity ke android
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }


        });



    }

    @Override
    public int getItemCount() {
        return recordList.size(); //return size of list/number or records
    }


    class HolderRecord extends RecyclerView.ViewHolder {

        //views
        TextView txtId, txtBray, txtCa, txtClay, txtCn, txtHclK2o, txtHclP2o5, txtJumlah, txtK, txtKbAdj,
                txtKjeldahl, txtKtk, txtMg, txtMorgan, txtNa, txtOlsen, txtPhh20, txtPhkcl, txtRetensip, txtSand,
                txtSilt, txtWbc;
        LinearLayout linear;


        public HolderRecord(@NonNull View itemView) {
            super(itemView);


            //init views
            txtId = itemView.findViewById(R.id.txtId);
            txtBray = itemView.findViewById(R.id.txtBray);
            txtCa = itemView.findViewById(R.id.txtCa);
            txtClay = itemView.findViewById(R.id.txtClay);
            txtCn = itemView.findViewById(R.id.txtCn);
            txtHclK2o = itemView.findViewById(R.id.txtHclK2o);
            txtHclP2o5 = itemView.findViewById(R.id.txtHclP2o5);
            txtJumlah = itemView.findViewById(R.id.txtJumlah);
            txtK = itemView.findViewById(R.id.txtK);
            txtKbAdj = itemView.findViewById(R.id.txtKbAdj);
            txtKjeldahl = itemView.findViewById(R.id.txtKjeldahl);
            txtKtk = itemView.findViewById(R.id.txtKtk);
            txtMg = itemView.findViewById(R.id.txtMg);
            txtMorgan = itemView.findViewById(R.id.txtMorgan);
            txtNa = itemView.findViewById(R.id.txtNa);
            txtOlsen = itemView.findViewById(R.id.txtOlsen);
            txtPhh20 = itemView.findViewById(R.id.txtPhh20);
            txtPhkcl = itemView.findViewById(R.id.txtPhkcl);
            txtRetensip = itemView.findViewById(R.id.txtRetensip);
            txtSand = itemView.findViewById(R.id.txtSand);
            txtSilt = itemView.findViewById(R.id.txtSilt);
            txtWbc = itemView.findViewById(R.id.txtWbc);
        }
    }

}
