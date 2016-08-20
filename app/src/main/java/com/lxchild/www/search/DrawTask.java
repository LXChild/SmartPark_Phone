package com.lxchild.www.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.lxchild.www.algorithm.Algorithm;
import com.lxchild.www.smartpark.R;
import com.lxchild.www.util.MyUtil;
import com.lxchild.www.widget.ScaleableImageView;

import java.util.HashMap;

/**
 * Created by LXChild on 2015/4/11.
 */
public class DrawTask extends AsyncTask<Void, Void, Bitmap> {
    private String TAG = DrawTask.class.getSimpleName();
    private Context cxt;

    private ScaleableImageView iv_scale;

    public DrawTask(ScaleableImageView iv_scale, Context cxt) {

        this.iv_scale = iv_scale;
        this.cxt = cxt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MyUtil.ShowProgressDialog(cxt);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bmp = null;
        try {
            bmp = useDoubleBuffer();
            Canvas cvs = new Canvas(bmp);
            // cvs.drawColor(Color.WHITE);
            cvs.drawBitmap(bmp, 0, 0, null);
            drawPath(cvs);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        MyUtil.DismissProgressDialog();
        if (bitmap != null) {
            iv_scale.setImageBitmap(bitmap);
        }
        this.cancel(true);
    }

    private void drawPath(Canvas cvs) {
        Algorithm algorithm = SearchActivity.getAlgorithm();

        if (algorithm != null) {
            if (algorithm.isPathFlag()) {

                Bitmap bmp_source = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.source);
                Bitmap bmp_target = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.target);

                Paint paint = new Paint();
                int span = 27;

                HashMap<String, int[][]> hm = algorithm.getHm();
                int[] temp = algorithm.getTarget();
                while (true) {
                    int[][] tempA = hm.get(temp[0] + ":" + temp[1]);
                    paint.setColor(Color.GREEN);
                    paint.setStyle(Paint.Style.STROKE);//加粗
                    paint.setStrokeWidth(8);//设置画笔粗度为2px
                    cvs.drawLine(
                            tempA[0][0] * (span + 1) + span / 2 + 6, tempA[0][1] * (span + 1) + span / 2 + 6,
                            tempA[1][0] * (span + 1) + span / 2 + 6, tempA[1][1] * (span + 1) + span / 2 + 6,
                            paint
                    );

                    if (tempA[1][0] == algorithm.getSource()[0] && tempA[1][1] == algorithm.getSource()[1]) {//判断有否到出发点
                        break;
                    }
                    temp = tempA[1];
                }

                //绘制出发点
                cvs.drawBitmap(bmp_source, 6 + algorithm.getSource()[0] * (span + 1) - bmp_source.getWidth() / 2 + 3, 6 + algorithm.getSource()[1] * (span + 1) - bmp_source.getHeight() + 3, paint);
                //绘制目标点
                cvs.drawBitmap(bmp_target, 6 + algorithm.getTarget()[0] * (span + 1) - bmp_target.getWidth() / 2, 6 + algorithm.getTarget()[1] * (span + 1) - bmp_target.getHeight(), paint);
            }
        }
    }

    private Bitmap useDoubleBuffer() {
        Canvas cvs = new Canvas();
        Paint paint = new Paint();
        try {
//            recycleBitmap(bmp_src);
//            recycleBitmap(bmp_buffer);
            /**加载资源*/
            Bitmap bmp_src = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.map);
            /**创建图片大小的缓冲区*/
            Bitmap bmp_buffer = Bitmap.createBitmap(bmp_src.getWidth(), bmp_src.getHeight(), Bitmap.Config.ARGB_8888);
            /**设置将bmp_src绘制在bmp_buffer上*/
            cvs.setBitmap(bmp_buffer);
            /**将bmp_src绘制在bmp_buffer上*/
            cvs.drawBitmap(bmp_src, 0, 0, paint);
            return bmp_buffer;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }
}
