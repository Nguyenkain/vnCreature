package com.vncreatures.customItems;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.markupartist.android.widget.PullToRefreshListView;

public class RelativeLayoutCustom extends RelativeLayout {

    public RelativeLayoutCustom(Context context) {
        super(context);
    }

    public RelativeLayoutCustom(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RelativeLayoutCustom(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        
        Rect bgPadding = new Rect();
        getBackground().getPadding(bgPadding);
        height += bgPadding.top + bgPadding.bottom;
        setMeasuredDimension(width, height);
    }
}