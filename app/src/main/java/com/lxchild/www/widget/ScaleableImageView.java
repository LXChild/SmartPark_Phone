package com.lxchild.www.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class ScaleableImageView extends ImageView implements OnGlobalLayoutListener,
        OnScaleGestureListener, OnTouchListener {

    private String TAG = ScaleableImageView.class.getSimpleName();

    private boolean once;

    /**
     * 初始化时缩放的值
     */
    private float initScale;
    /**
     * 双击放大时到达的值
     */
    private float midScale;
    /**
     * 放大到达的最大值
     */
    private float maxScale;

    private Matrix scaleMatrix;
    /**
     * 捕获用户多指触控时缩放的比例
     */
    private ScaleGestureDetector sgd;

    //自由移动
    /**
     * 记录上一次多点触控的数量
     */
    private int lastPointerCount;

    private float lastX;
    private float lastY;

    private int touchSlop;

    private boolean isCanDrag;

    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    //双击放大缩小
    private GestureDetector gestureDetector;

    private boolean isAutoScale;

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    public ScaleableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        scaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        sgd = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if (isAutoScale) {
                    return true;
                }
                float x = e.getX();
                float y = e.getY();
                if (getScale() < midScale) {
//					scaleMatrix.postScale(midScale / getScale(), midScale / getScale(), x, y);
//					setImageMatrix(scaleMatrix);
                    postDelayed(new AutoScaleRunnable(midScale, x, y), 16);
                    isAutoScale = true;
                } else {
//					scaleMatrix.postScale(initScale / getScale(), initScale / getScale(), x, y);
//					setImageMatrix(scaleMatrix);
                    postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
                    isAutoScale = true;
                }

                return true;
            }

//            @Override
//            public void onLongPress(MotionEvent e) {
//                MapBitmap.hasTarget = true;
//                MapBitmap.target_pos[0] = e.getX();
//                MapBitmap.target_pos[1] = e.getY();
//                Log.d(TAG, "OnLongPressed:" + e.getRawX() + "," + e.getRawY());
//                super.onLongPress(e);
//            }
        });
    }

    private class AutoScaleRunnable implements Runnable {

        /**
         * 缩放的目标值
         */
        private float targetScale;
        //缩放的中心点
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        private float tempScale;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.targetScale = targetScale;
            this.x = x;
            this.y = y;

            if (getScale() < targetScale) {
                tempScale = BIGGER;
            }
            if (getScale() > targetScale) {
                tempScale = SMALL;
            }
        }

        @Override
        public void run() {
            //进行缩放
            scaleMatrix.postScale(tempScale, tempScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(scaleMatrix);

            float currentScale = getScale();
            if ((tempScale > 1.0f && currentScale < targetScale) || (tempScale < 1.0f && currentScale > targetScale)) {
                postDelayed(this, 16);
            } else {//达到目标值
                float scale = targetScale / currentScale;
                scaleMatrix.postScale(scale, scale, x, y);
                setImageMatrix(scaleMatrix);

                isAutoScale = false;
            }
        }
    }

    public ScaleableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleableImageView(Context context) {
        this(context, null);
    }

    /**
     * 当布局加载到窗口时调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 注册GlobalLayout
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 当布局被移除窗口时调用
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 注销GlobalLayout
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 全局布局完成后调用
     * <p/>
     * 获取ImageView加载完成后的图片
     */
    @Override
    public void onGlobalLayout() {
        if (!once) {
            // 得到控件的宽和高，一般为屏幕高度
            int width = getWidth();
            int height = getHeight();
            // 得到我们的图片，以及宽和高
            Drawable d = getDrawable();
            if (d == null) {
                return;
            }
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
//			Log.d(TAG, width + "," + height + "," + dw + "," + dh);

            /**
             * 将图片宽高与控件宽高对比，进行缩放
             * */
            float scale = 1.0f;
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }

            if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            }

            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            /**
             * 得到了初始化时缩放的比例
             * */
            initScale = scale;
            maxScale = initScale * 4;
            midScale = initScale * 2;

            // 将图片移动至控件中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            scaleMatrix.postTranslate(dx, dy);
            scaleMatrix.postScale(initScale, initScale, width / 2, height / 2);
            setImageMatrix(scaleMatrix);

            once = true;
        }
    }

    /**
     * 获取当前图片缩放值
     */
    public float getScale() {

        float[] values = new float[9];
        scaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return true;
        }

        // 缩放范围控制
        if ((scale < maxScale && scaleFactor > 1.0f)
                || (scale > initScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < initScale) {
                scaleFactor = initScale / scale;
            }

            if (scale * scaleFactor > maxScale) {
                scale = maxScale / scale;
            }
            // 设置缩放参数及缩放中心
            scaleMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(scaleMatrix);
        }

        return true;
    }

    private RectF getMatrixRectF() {
        Matrix matrix = scaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        return rectF;
    }

    /**
     * 在缩放时进行边界和位置的控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        //缩放时进行边界检测，防止出现白边
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }
        //如果宽度或高度小于控件的宽或者高，则让其居中
        if (rectF.width() < width) {
            deltaX = width / 2 - rectF.right + rectF.width() / 2;
        }
        if (rectF.height() < height) {
            deltaY = height / 2 - rectF.bottom + rectF.height() / 2;
        }
        scaleMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }

        sgd.onTouchEvent(event);

        float x = 0;
        float y = 0;
        //拿到多点触控的数量
        int pointCount = event.getPointerCount();
        for (int i = 0; i < pointCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointCount;
        y /= pointCount;

        if (lastPointerCount != pointCount) {

            isCanDrag = false;
            lastX = x;
            lastY = y;
        }
        lastPointerCount = pointCount;

        RectF rectF = getMatrixRectF();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
//                    if (getParent() instanceof VerticalViewPager) {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                }
                break;

            case MotionEvent.ACTION_MOVE:
//                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
//                    if (getParent() instanceof VerticalViewPager) {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                }

                float dx = x - lastX;
                float dy = y - lastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                } else {
                    if (getDrawable() != null) {

                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        //如果宽度小于控件宽度，不允许横向移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        //如果高度小于空间高度，不允许纵向移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        scaleMatrix.postTranslate(dx, dy);

                        checkBorderWhenTranslate();
                        setImageMatrix(scaleMatrix);
                    }
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 当移动时进行边界检查
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }
        scaleMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 判断是否足以触发move
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > touchSlop;
    }
}
