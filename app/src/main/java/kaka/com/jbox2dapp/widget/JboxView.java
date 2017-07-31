package kaka.com.jbox2dapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by kaka on 2017/7/25.
 */
public class JboxView extends FrameLayout{
    private JboxImpl jboxImpl;

    public JboxView(Context context) {
        this(context,null);
    }

    public JboxView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JboxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        intView();
    }

    private void intView() {
        jboxImpl = new JboxImpl(getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        jboxImpl.startWorld();
        int childCount = getChildCount();
        for (int i =0; i< childCount; i++) {
            View view = getChildAt(i);
            if (jboxImpl.isBodyView(view)) {
                view.setX(jboxImpl.getViewX(view));
                view.setY(jboxImpl.getViewY(view));
                view.setRotation(jboxImpl.getViewRotaion(view));
            }
        }
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        jboxImpl.createWorld();
        //子viwe创建tag 设置body
        int childCount = getChildCount();
        for (int i =0; i< childCount; i++) {
            View view = getChildAt(i);
            //body为空时创建刚体
            if (!jboxImpl.isBodyView(view) || changed) {
                jboxImpl.creatBody(view);
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        jboxImpl.setWorlSize(w,h);
    }

    /**
     * 给每个刚体重力运动坐标
     * @param x
     * @param y
     */
    public void onSensorChanged(float x, float y) {
        int childCount = getChildCount();
        for (int i =0; i< childCount; i++) {
            View view = getChildAt(i);
            if (jboxImpl.isBodyView(view)) {
                jboxImpl.applyLinearImpulse(x,y,view);
            }
        }
    }
}
