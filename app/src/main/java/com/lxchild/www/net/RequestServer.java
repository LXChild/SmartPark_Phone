package com.lxchild.www.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by LXChild on 2015/7/5.
 */
public class RequestServer extends AsyncTask<String, Void, String> {
    private final String TAG = RequestServer.class.getSimpleName();
    private Context cxt;
    private String result;

    private ProgressDialog progressDialog;

    public RequestServer(Context cxt) {
        this.cxt = cxt;
    }

    @Override
    protected void onPreExecute() {
        showProgressDialog(cxt);
        super.onPreExecute();
    }
    /**
     * @param params 0 url, 1 param_name1, 1 param_value2...
     * */
    @Override
    protected String doInBackground(String... params) {
        return runHTTPPost(params);
    }

    @Override
    protected void onPostExecute(String s) {
        result = s;
        progressDialog.dismiss();
        Log.d(TAG, s);
        super.onPostExecute(s);
    }

    public String getResult() {
        return result;
    }

    private String runHTTPPost(String[] params) {
        //发送请求
        try {
            HttpPost postMethod = new HttpPost(params[0]);
            if (params.length > 1) {
                //先将参数放入List，再对参数进行URL编码
                List<BasicNameValuePair> post_params = new LinkedList<>();
                for (int i = 1; i < params.length; i += 2) {
                    post_params.add(new BasicNameValuePair(params[i], params[i + 1]));
                }
                postMethod.setEntity(new UrlEncodedFormEntity(post_params, "utf-8")); //将参数填入POST Entity中
            }

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

    private void showProgressDialog(Context cxt) {
        progressDialog = new ProgressDialog(cxt);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("请稍候");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
