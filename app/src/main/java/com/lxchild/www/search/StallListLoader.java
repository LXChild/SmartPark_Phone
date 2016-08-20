package com.lxchild.www.search;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.lxchild.www.util.MyUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LXChild on 2015/5/3.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StallListLoader extends AsyncTaskLoader<ArrayList<StallEntity>> {
    private String TAG = StallListLoader.class.getSimpleName();
    private Context cxt;

    private ArrayList<StallEntity> stallInfos;
    private ArrayList<StallEntity> stallInfos_loaded;

    private String filter;

    public StallListLoader(Context context, String filter) {
        super(context);
        this.cxt = context;
        this.filter = filter;
    }

    @Override
    protected void onStartLoading() {
        MyUtil.ShowProgressDialog(cxt);
        forceLoad();
        if (stallInfos_loaded != null) {
            deliverResult(stallInfos_loaded);
        }
        super.onStartLoading();
    }

    @Override
    public ArrayList<StallEntity> loadInBackground() {
        if (stallInfos == null) {
            stallInfos = new ArrayList<>();
        }

        String URL = "http://www.lxchildformtest.sinaapp.com/queryStall.php";
        ArrayList<StallEntity> stalls = parseJSON(runHTTPPost(URL));

        if (stalls != null) {
            for (StallEntity info : stalls) {
                addStallInfo(info);
            }
        }

        return stallInfos;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCanceled(ArrayList<StallEntity> data) {
        super.onCanceled(data);
        cancelLoad();
        MyUtil.DismissProgressDialog();
    }

    @Override
    protected void onStopLoading() {
        MyUtil.DismissProgressDialog();
        super.onStopLoading();
    }

    @Override
    public void deliverResult(ArrayList<StallEntity> data) {
        MyUtil.DismissProgressDialog();
        if (isReset()) {
            if (data != null) {
                data.clear();
                data = null;
            }
        }
        stallInfos_loaded = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onReset() {
        MyUtil.ShowProgressDialog(cxt);
        onStopLoading();
        if (stallInfos_loaded != null) {
            stallInfos_loaded = null;
        }
        super.onReset();
    }

    private void addStallInfo(StallEntity info) {
        if (filter != null && !filter.trim().equals("") && info != null ) {
            if (((info.getId() + "").trim().contains(filter)) || (filter.contains((info.getId() + "").trim()))) {
                stallInfos.add(info);
            }
        } else {
            stallInfos.add(info);
        }
    }

    private String runHTTPPost(String URL) {
        //发送请求
        try {
            HttpPost postMethod = new HttpPost(URL);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(postMethod); //执行POST方法
            if (response.getStatusLine().getStatusCode() == 200) {
                //返回读到的数据
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<StallEntity> parseJSON(String s) {
        if (s != null && !s.trim().equals("")) {
            try {
                Log.d(TAG, s);
                JSONObject json = new JSONObject(s);
                String result = json.getString("result");
                if (result.equals("succeed")) {
                    ArrayList<StallEntity> stalls = new ArrayList<>();
                    int count = json.getInt("count");
                    JSONArray arr = json.getJSONArray("data");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject data = arr.getJSONObject(i);
                        StallEntity stall = new StallEntity();
                        if (data.has("id") && !data.isNull("id")) {
                            stall.setId(data.getInt("id"));
                        }
                        if (data.has("status") && !data.isNull("status")) {
                            stall.setStatus(data.getString("status"));
                        }

                        stall.setPos(new int[]{38, 33});

                        stalls.add(stall);
                    }
                    return stalls;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
