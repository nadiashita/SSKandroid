package com.si_ware.neospectra.myViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.si_ware.neospectra.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class MySeekBar extends android.support.v7.widget.AppCompatSeekBar {

    private List<ProgressItem> mProgressItemsList;

    public MySeekBar(Context context) {
        super(context);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }

    public void initData(ArrayList<ProgressItem> progressItemsList) {
        this.mProgressItemsList = progressItemsList;
    }


    @Override
    protected synchronized void onDraw(@NonNull Canvas canvas) {
        int progressBarWidth = getWidth();
        int progressBarHeight = getHeight();
        int thumboffset = getThumbOffset();
        int lastProgressX = 0;
        int progressItemWidth, progressItemRight;
        for (int i = 0; i < mProgressItemsList.size(); i++) {
            ProgressItem progressItem = mProgressItemsList.get(i);
            Paint progressPaint = new Paint();
            progressPaint.setColor(getResources().getColor(progressItem.color));
            progressItemWidth = (int) (progressItem.progressItemPercentage
                    * progressBarWidth / 100);
            progressItemRight = lastProgressX + progressItemWidth;
            // for last item give right of the progress item to width of the
            // progress bar
            if (i == mProgressItemsList.size() - 1
                    && progressItemRight != progressBarWidth) {
                progressItemRight = progressBarWidth;
            }
            Rect progressRect = new Rect();
            progressRect.set(lastProgressX, thumboffset / 2, progressItemRight,
                    progressBarHeight - thumboffset / 2);
            canvas.drawRect(progressRect, progressPaint);
            lastProgressX = progressItemRight;
        }
        super.onDraw(canvas);
    }
}