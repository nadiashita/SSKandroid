package com.si_ware.neospectra.myViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.BaseExpandableListAdapter;

import com.si_ware.neospectra.R;

/**
 * Created by AmrWinter on 15/05/2017.
 */

abstract class SwipeToDelete extends ItemTouchHelper.SimpleCallback {
    private Drawable background;
    private Drawable deleteIcon;

    private int xMarkMargin;

    private boolean initiated;
    private Context context;

    private int leftColorCode;
    private String leftSwipeLabel;

    SwipeToDelete(int dragDirs, int swipeDirs, Context context) {
        super(dragDirs, swipeDirs);
        this.context = context;
    }

//    private void setSwipeForRecyclerView(/*Adapter*/ final BaseExpandableListAdapter adapter) {
//        SwipeToDelete swipeHelper = new SwipeToDelete(0, ItemTouchHelper.LEFT, context) {
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int swipedPosition = viewHolder.getAdapterPosition();
////                ShoppingAdapter adapter = (ShoppingAdapter) rvShoppingList.getAdapter();
//                adapter.pendingRemoval(swipedPosition);
//            }
//            @Override
//            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                int position = viewHolder.getAdapterPosition();
////                ShoppingAdapter adapter = (ShoppingAdapter) rvShoppingList.getAdapter();
//                if (adapter.isPendingRemoval(position)) {
//                    return 0;
//                }
//                return super.getSwipeDirs(recyclerView, viewHolder);
//            }
//        };
//        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(swipeHelper);
//        mItemTouchHelper.attachToRecyclerView(rvShoppingList);
//        //set swipe label
//        swipeHelper.setLeftSwipeLable(getResources().getString(R.string.delete_label));
//        //set swipe background-Color
//        swipeHelper.setLeftcolorCode(ContextCompat.getColor(context, R.color.swipebg));
//
//    }

    private void init() {
        background = new ColorDrawable();
        xMarkMargin = (int) context.getResources().getDimension(R.dimen.txtMargin);
        deleteIcon = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_delete);
        deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initiated = true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        if (!initiated) {
            init();
        }

        int itemHeight = itemView.getBottom() - itemView.getTop();
        //Setting Swipe Background
        ((ColorDrawable) background).setColor(getLeftColorCode());
        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                itemView.getRight(), itemView.getBottom());
        background.draw(c);

        int intrinsicWidth = deleteIcon.getIntrinsicWidth();
        int intrinsicHeight = deleteIcon.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = itemView.getRight() - xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int xMarkBottom = xMarkTop + intrinsicHeight;

        //Setting Swipe Icon
        deleteIcon.setBounds(xMarkLeft, xMarkTop + 16, xMarkRight, xMarkBottom);
        deleteIcon.draw(c);

        //Setting Swipe Text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(48);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(getLeftSwipeLabel(), xMarkLeft + 10, xMarkTop + 10, paint);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private String getLeftSwipeLabel() {
        return leftSwipeLabel;
    }

    void setLeftSwipeLabel(String leftSwipeLabel) {
        this.leftSwipeLabel = leftSwipeLabel;
    }

    private int getLeftColorCode() {
        return leftColorCode;
    }

    void setLeftColorCode(int leftColorCode) {
        this.leftColorCode = leftColorCode;
    }
}
