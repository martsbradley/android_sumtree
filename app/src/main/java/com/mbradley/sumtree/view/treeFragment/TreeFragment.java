package com.mbradley.sumtree.view.treeFragment;

//import android.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mbradley.sumtree.layout.EOrientation;
import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.CompleteStatus;
import com.mbradley.sumtree.sumdisplay.TreeButtonHandler;
import com.mbradley.sumtree.sumdisplay.SumDisplay;
import com.mbradley.sumtree.view.R;

public class TreeFragment extends LinearLayout implements TreeButtonHandler, TreeOrientationQuery
{
    private DrawView mDrawView;
    private SumDisplay mSumDisplay;
    private EOrientation mTreeOrientation = EOrientation.ETreeDown;

    public TreeFragment(Context aContext)
    {
        super(aContext);
        init(aContext);
    }

    public TreeFragment(Context aContext, AttributeSet attrs)
    {
        super(aContext, attrs);
        init(aContext);
    }

    public TreeFragment(Context aContext, AttributeSet attrs, int defStyle)
    {
        super(aContext, attrs, defStyle);
        init(aContext);
    }
    private void init(Context aContext)
    {
        View myView = inflate(aContext, R.layout.tree_fragment, this);
        mDrawView = (DrawView) myView.findViewById(R.id.drawView);

        mDrawView.setTreeOrientationQuery(this);

        if (aContext instanceof Activity)
        {
            mSumDisplay = (SumDisplay) aContext;
            mDrawView.setExpressionDisplay(mSumDisplay);
        }
    }

    public void expressionUpdated(String aExpression)
    {
        InitializationThread thread = new InitializationThread(this,mSumDisplay, aExpression);
        thread.execute();
    }

//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        mDrawView.setMotionHandler(mDrawView);
//    }

    public String getExpression()
    {
        return mDrawView.getExpression();
    }

    public void setTree(Shape aTree)
    {
        mDrawView.setTree(aTree);
    }

    public void setTree(Shape aTree, int width, int height)
    {
        mDrawView.setSize(width, height);
        setTree(aTree);
    }

    public void zeroDivisionAttempt()
    {
        mDrawView.showDivideByZero();
    }

    public void infinity()
    {
        mDrawView.showInfinity();
    }

    public void collapsedUnFinishedTree(CompleteStatus aCompleteStatus)
    {
        mDrawView.collapsedUnFinishedTree(aCompleteStatus);
    }

    public Shape getTree()
    {
        return mDrawView.getTree();
    }

    @Override
    public void reset()
    {
        expressionUpdated("0");
    }
    @Override
    public void flipTree()
    {
        toggleTreeOrientation();

        expressionUpdated(getExpression());
    }

    private void toggleTreeOrientation()
    {
        if (mTreeOrientation == EOrientation.ETreeDown)
            mTreeOrientation = EOrientation.ETreeUp;
        else
            mTreeOrientation = EOrientation.ETreeDown;
    }

    @Override
    public EOrientation getTreeOrientation()
    {
        return mTreeOrientation;
    }

    public int getDrawViewWidth()
    {
        return mDrawView.getScaledWidth();
    }
}
