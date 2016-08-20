package com.lxchild.www.search;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.lxchild.www.algorithm.Algorithm;
import com.lxchild.www.smartpark.R;

import java.util.ArrayList;

/**
 * Created by LXChild on 2015/7/5.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<ArrayList<StallEntity>>, SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private ListView lv_stall;
    private StallListAdapter adapter;
    private String mCurFilter;

    private static Algorithm algorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initData();
    }

    private void initView() {
        lv_stall = (ListView) findViewById(R.id.lv_search);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initData() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<StallEntity>> onCreateLoader(int id, Bundle args) {
        return new StallListLoader(this, mCurFilter);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<StallEntity>> loader, ArrayList<StallEntity> data) {
        adapter = new StallListAdapter(this);
        adapter.setData(data);
        lv_stall.setAdapter(adapter);
        lv_stall.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
        lv_stall.setOnItemClickListener(this);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<StallEntity>> loader) {
        adapter.setData(null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        showActionBar();
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        algorithm = new Algorithm();
//        if (algorithm.isCancelled()) {
            algorithm.setCxt(this);
            algorithm.setTarget(adapter.getItem(position).getPos());
            algorithm.setSource(new int[]{22, 1});
            algorithm.execute();
//        } else {
//            Toast.makeText(this, "算法正在运行中，请稍后再试", Toast.LENGTH_SHORT).show();
//        }
        finish();
    }

    public static Algorithm getAlgorithm() {
        return algorithm;
    }
}
