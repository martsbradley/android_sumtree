package com.mbradley.sumtree.view.treeFragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
//import android.support.v4.content.res.ResourcesCompat;
//import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MotionEventCompat;

import com.mbradley.sumtree.sumdisplay.TreeButtonHandler;
import com.mbradley.sumtree.view.R;

public class ClearButtonView extends View
{
    private final Drawable mClearIcon ;
    private final Rect mClearBounds  = new Rect();
    private TreeButtonHandler mTreeButtonHandler = null;
    public ClearButtonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mClearIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.clear, null);
    }
    public void setTreeButtonHandler(TreeButtonHandler aTreeButtonHandler)
    {
        this.mTreeButtonHandler = aTreeButtonHandler;
    }

    @Override
    protected void onDraw(Canvas aCanvas)
    {
        int height = getHeight();
        int boxSide = Math.min(height, getWidth());

        mClearBounds.set(0,
                         0,
                         boxSide,
                         boxSide);
        mClearIcon.setBounds(mClearBounds);
        mClearIcon.draw(aCanvas);


    }

   @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        int action = MotionEventCompat.getActionMasked(event);
        int x =  (int)event.getX();
        int y =  (int)event.getY();

        switch(action)
        {

            case (MotionEvent.ACTION_DOWN) :
                wasButtonPressed(x, y);
                return true;

            default :
                return super.onTouchEvent(event);
        }
    }

    private void wasButtonPressed(int aX, int aY)
    {
        if (mClearBounds.contains(aX,aY))
        {
            //Log.e("cli", "clear");
            mTreeButtonHandler.reset();
        }
    }
}
