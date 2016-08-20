package com.lxchild.www.inpark;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lxchild.www.search.DrawTask;
import com.lxchild.www.search.SearchActivity;
import com.lxchild.www.smartpark.R;
import com.lxchild.www.widget.ScaleableImageView;

/**
 * Created by LXChild on 2015/7/5.
 */
public class InParkFragment extends Fragment {
    private String TAG = InParkFragment.class.getSimpleName();
    private ScaleableImageView iv_scale;
    private DrawTask drawTask;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inpark, container, false);
        iv_scale = (ScaleableImageView) rootView.findViewById(R.id.iv_scale);
        setMap();
        return rootView;
    }

    private void setMap() {
        drawTask = new DrawTask(iv_scale, this.getActivity());
        drawTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inpark, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_inpark_search) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (drawTask.isCancelled()) {
            setMap();
        }
    }
}
