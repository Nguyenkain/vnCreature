package com.example.vncreatures.customItems;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.alhneiti.QuickAction.QuickAction;
import com.alhneiti.QuickAction.QuickActionBar;
import com.alhneiti.QuickAction.QuickActionWidget;
import com.alhneiti.QuickAction.QuickActionWidget.OnQuickActionClickListener;
import com.example.vncreatures.R;

public class DiscussionQuickAction {
    private Context mContext;
    private QuickActionWidget mBar;
    private Callback mCallback = null;

    public interface Callback {
        public void onQuickActionClicked(QuickActionWidget widget, int position);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public DiscussionQuickAction(Context context) {
        this.mContext = context;
        prepareQuickActionBar();
    }

    public void onShowBar(View v) {
        mBar.show(v);
    }

    private void prepareQuickActionBar() {
        mBar = new QuickActionBar(mContext);
        mBar.addQuickAction(new MyQuickAction(mContext,
                R.drawable.gd_action_bar_compose, R.string.edit));
        mBar.addQuickAction(new MyQuickAction(mContext,
                R.drawable.content_discard, R.string.delete));
        mBar.addQuickAction(new MyQuickAction(mContext,
                R.drawable.rating_bad, R.string.report));

        mBar.setOnQuickActionClickListener(mActionListener);
    }

    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
            if (mCallback != null) {
                mCallback.onQuickActionClicked(widget, position);
            }
        }
    };

    private static class MyQuickAction extends QuickAction {

        private static final ColorFilter BLACK_CF = new LightingColorFilter(
                Color.BLACK, Color.BLACK);

        public MyQuickAction(Context ctx, int drawableId, int titleId) {
            super(ctx, buildDrawable(ctx, drawableId), titleId);
        }

        private static Drawable buildDrawable(Context ctx, int drawableId) {
            Drawable d = ctx.getResources().getDrawable(drawableId);
            d.setColorFilter(BLACK_CF);
            return d;
        }

    }
}
