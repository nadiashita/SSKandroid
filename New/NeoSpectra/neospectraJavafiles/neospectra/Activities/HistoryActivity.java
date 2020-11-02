package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.si_ware.neospectra.Adapters.ExpandableAdapter;
import com.si_ware.neospectra.Models.dbResult;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import java.util.ArrayList;

public class HistoryActivity extends NavDrawerActivity {
    private static final String TAG = "HistoryActivity";
    DrawerLayout drawer;

    private ExpandableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawer = findViewById(R.id.drawer_layout);
        final LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_history, null, false);
        drawer.addView(contentView, 0);
        listView = findViewById(R.id.history);
        ScanPresenter presenter = new ScanPresenter();
        final ArrayList<dbResult> results = presenter.readResults(this);
        final ExpandableAdapter adapter = new ExpandableAdapter(this, results);
        listView.setAdapter(adapter);
        SearchView search = findViewById(R.id.sv_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listView.setAdapter(new ExpandableAdapter(getBaseContext(), Filter(results, query)));
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                listView.setAdapter(new ExpandableAdapter(getBaseContext(), Filter(results, newText)));
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        // Retrieve the SearchView and plug it into SearchManager
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private ArrayList<dbResult> Filter(ArrayList<dbResult> list, String query){
        ArrayList<dbResult> newList = new ArrayList<>();
        for (dbResult result : list) {
            if (result.getModuleName().toLowerCase().startsWith(query.toLowerCase())
                    || result.getTimestamp().contains(query)){
                newList.add(result);
            }
        }
//        Log.v(TAG, "newList size" + newList.size());
        return newList;
    }
}
