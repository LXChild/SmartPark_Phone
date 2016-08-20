package com.lxchild.www.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by LXChild on 2015/7/5.
 */
public class MyUtil {

    private static ProgressDialog progressDialog;

    public static void ShowProgressDialog(Context cxt) {
        progressDialog = new ProgressDialog(cxt);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("请稍候");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public static void DismissProgressDialog() {
        progressDialog.dismiss();
    }
}
