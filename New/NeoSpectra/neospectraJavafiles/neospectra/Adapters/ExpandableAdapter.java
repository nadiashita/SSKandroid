package com.si_ware.neospectra.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si_ware.neospectra.DataIO.DataIO;
import com.si_ware.neospectra.Models.dbResult;
import com.si_ware.neospectra.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.si_ware.neospectra.Global.Global.FILE_NAME_RESULTS;

/**
 * Created by AmrWinter on 1/5/18.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private static List<String> headerList; //timeStamp, array of timeStamps
    private static HashMap<String, String> headerHash; // timeStamp, ModuleName
    private static HashMap<String, ArrayList<dbResult.dbVarResult>> bodyList; // <timeStamp, <String[2]{varName, varValue}>>
    private static ArrayList<dbResult> results;
    private final String TAG = "adapter";

    public ExpandableAdapter(Context mContext, ArrayList<dbResult> resultsList){
        this.mContext = mContext;
        results = resultsList;
        // Sort the results
//        Collections.sort(results, new Comparator<dbResult>() {
//            @Override
//            public int compare(dbResult lhs, dbResult rhs) {
//                return lhs.getTimestamp().compareTo(rhs.getTimestamp());
//            }
//        });
        reFormatResults(results);
    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String tmp1 = headerList.get(groupPosition);
        return bodyList.get(tmp1).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bodyList.get(headerList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition,
                             boolean isExpanded,
                             View convertView,
                             ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this
                    .mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_header, null);
        }
        TextView moduleName, timeStamp;
        ImageView delete = convertView.findViewById(R.id.iv_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAskToDeleteItem(mContext, groupPosition);
            }
        });
        moduleName = convertView.findViewById(R.id.tv_module_name);
        timeStamp = convertView.findViewById(R.id.tv_timestamp);
        String headerTimeStamp = (String) getGroup(groupPosition);
        String headerTitle = headerHash.get(headerTimeStamp);
        moduleName.setTypeface(null, Typeface.BOLD);
        moduleName.setText(headerTitle);
        timeStamp.setTypeface(null, Typeface.ITALIC);
        timeStamp.setText(headerTimeStamp);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent) {
        final dbResult.dbVarResult varResult = (dbResult.dbVarResult) getChild(groupPosition, childPosition);
        TextView varName, varValue;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this
                    .mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_body, null);
        }
        varName = convertView.findViewById(R.id.tvVarName);
        varValue = convertView.findViewById(R.id.tvVarValue);

        varName.setTypeface(null, Typeface.BOLD);
        varName.setText(varResult.getVarName());

        varValue.setTypeface(null, Typeface.NORMAL);
        varValue.setText(varResult.getVarResult().toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void showAskToDeleteItem(final Context mContext, final int position){
        String title = mContext.getString(R.string.title_delete_item);
        String body = mContext.getString(R.string.ask_to_delete_item);
        android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                .Builder(mContext);
        myAlert.setTitle(title);
        myAlert.setMessage(body);
        myAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
//                ArrayList<dbResult> newResults = ;
                //Save the new list in file
                saveNewResults(mContext, deleteItem(position));
            }
        });
        myAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        myAlert.show();
    }

    private ArrayList<dbResult> deleteItem(int position) {
        ArrayList<dbResult> newList = new ArrayList<>();
        for (dbResult result : results) {
            if (!result.getTimestamp().equals(getGroup(position))){
                newList.add(result);
            }
        }
        results = newList;
        reFormatResults(results);
        notifyDataSetChanged();
        return results;
    }

    private void reFormatResults(ArrayList<dbResult> results) {
        headerHash = new HashMap<>();
        bodyList = new HashMap<>();
        // init Headers
        for (dbResult result :
                results) {
            headerHash.put(result.getTimestamp(), result.getModuleName());
            bodyList.put(result.getTimestamp(), result.getResults());
        }
        headerList = new ArrayList<>(headerHash.keySet());
    }

    public void saveNewResults(Context context, ArrayList<dbResult> results) {
        // To retrieve the object type
        Type type = new TypeToken<ArrayList<dbResult>>(){}.getType();
        String jsonData = new Gson().toJson(results, type);
//        Log.v("Gson", jsonData);
        DataIO.writeStringAsFile(context, FILE_NAME_RESULTS, jsonData);
    }

//    public void setFilter(List<dbResult> list){
//        results = list;
//        results.clear();
//        results.addAll(list);
//        Log.v(TAG, "list size" + list.size());
//        Log.v(TAG, "results size" + results.size());
//        this.notifyDataSetChanged();
//    }
}
