package com.mbradley.sumtree.view.treeFragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mbradley.sumtree.sumdisplay.TreeButtonHandler;
import com.mbradley.sumtree.view.R;

public class FlipButtonView extends View
{
    private final Drawable mFlipIcon ;
    private final Rect mFlipBounds  = new Rect();
    private TreeButtonHandler mTreeButtonHandler = null;
    public FlipButtonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mFlipIcon  = ResourcesCompat.getDrawable(getResources(), R.drawable.flip, null);
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

        mFlipBounds.set(0,
                        0,
                        boxSide,
                        boxSide);
        mFlipIcon.setBounds(mFlipBounds);
        mFlipIcon.draw(aCanvas);
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
        if (mFlipBounds.contains(aX,aY))
        {
            Log.e("cli", "filp");
            mTreeButtonHandler.flipTree();
        }
    }
}
