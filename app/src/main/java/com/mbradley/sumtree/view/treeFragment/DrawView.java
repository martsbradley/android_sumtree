package com.mbradley.sumtree.view.treeFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mbradley.sumtree.layout.EOrientation;
import com.mbradley.sumtree.layout.shape.DivideByZeroException;
import com.mbradley.sumtree.layout.shape.InfinityException;
import com.mbradley.sumtree.layout.shape.IntegerOperation;
import com.mbradley.sumtree.layout.shape.Point;
import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.CompleteStatus;
import com.mbradley.sumtree.layout.shape.visitor.NodeOverlapPrevention;
import com.mbradley.sumtree.layout.shape.visitor.ShapesOnEachLevel;
import com.mbradley.sumtree.layout.shape.visitor.NodeWidthVisitor;
import com.mbradley.sumtree.layout.shape.visitor.ShapeContainsVisitor;
import com.mbradley.sumtree.layout.shape.visitor.SumHighlightVisitor;
import com.mbradley.sumtree.layout.shape.visitor.andriod.DrawVisitor;
import com.mbradley.sumtree.model.MotionHandler;
import com.mbradley.sumtree.sumdisplay.SumDisplay;

import java.util.List;
import java.util.Map;

public class DrawView extends View implements MotionHandler
{
    private final MotionHandler mMotionHandler;
    private TreeOrientationQuery mTreeOrientationQuery;
    private Shape mTree = new IntegerOperation("0");
    private float mScaleFactor = 1.f;
    private boolean mInside = false;
    private SumDisplay mSumDisplay;
    private int mTreeWidth, mTreeHeight;
    private final Paint mBlackLinePaint = new Paint();

    private final DrawVisitor mDrawer;

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
       // mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        //   A temporary non null handler is needed to avoid null pointer problems.
        mMotionHandler = this;//new MotionHandler()
//        {
//            @Override
//            public void actionDown(int aX, int aY, int aUnscaledX, int aUnscaledY){}
//
//            @Override
//            public void actionMove(int aX, int aY){}
//        };

        mBlackLinePaint.setColor(Color.BLACK);
        mBlackLinePaint.setStyle(Paint.Style.STROKE);
        mBlackLinePaint.setStrokeWidth(2);
        mDrawer = new DrawVisitor(context);
    }
//    public void setMotionHandler(MotionHandler aMotionHandler)
//    {
//        mMotionHandler = aMotionHandler;
//    }

    @Override
    protected void onDraw(Canvas aCanvas)
    {
        super.onDraw(aCanvas);

        aCanvas.save();
        aCanvas.scale(mScaleFactor, mScaleFactor);
        EOrientation treeOrientation = mTreeOrientationQuery.getTreeOrientation();

        if (mTree != null)
        {
            //  long time = System.currentTimeMillis();
            mDrawer.setCanvas(aCanvas);

            mDrawer.setOrientation(treeOrientation);

            mTree.accept(mDrawer);

            boolean mShowGrid = false;
            if (mShowGrid)
            {
                mDrawer.drawGrid();
            }

            //  Log.e("drawtree took", (System.currentTimeMillis() - time)+" ");
        }
        aCanvas.restore();


    }


    public void setSize(int aTreeWidth, int aTreeHeight)
    {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        mTreeWidth = aTreeWidth;
        mTreeHeight = aTreeHeight;

        float horizScale = 1;
        float vertScale = 1;

        if (aTreeWidth > viewWidth)
        {
            horizScale = (float)viewWidth/(float)aTreeWidth;
        }
        if (aTreeHeight > viewHeight)
        {
            vertScale = (float)viewHeight/(float)aTreeHeight;
        }


//        Log.e("landscape", isLandscape() + " ");


        mScaleFactor = Math.min(horizScale, vertScale);
//        Log.d("DrawView","Screen(" + viewWidth + "," + viewHeight +") " + mScaleFactor);
    }
    @Override
    public void onSizeChanged (int w,
                        int h,
                        int oldw,
                        int oldh)
    {
       super.onSizeChanged (w,
                        h,
                        oldw,
                        oldh);

        setSize(mTreeWidth, mTreeHeight);
    }

    public void setTree(Shape aShape)
    {
        mTree = aShape;
        invalidate();
    }
    public Shape getTree()
    {
        return mTree;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = MotionEventCompat.getActionMasked(event);
        int unscaledX =  (int)event.getX();
        int unscaledY =  (int)event.getY();

        int x = (int)(unscaledX / mScaleFactor);
        int y = (int)(unscaledY / mScaleFactor);

        switch(action)
        {
            case (MotionEvent.ACTION_UP):
                mSumDisplay.clearExtents();
                return true;
            case (MotionEvent.ACTION_DOWN) :
                mMotionHandler.actionDown(x, y, unscaledX, unscaledY);
                return true;
            case (MotionEvent.ACTION_MOVE) :
                mMotionHandler.actionMove(x, y);
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    /* Updates from this DrawView can change the tree
     * and the expression will change, so this object
     * knows of a SumDisplay that it needs to inform.
     * @param aSumDisplay
     */
    public void setExpressionDisplay(SumDisplay aSumDisplay)
    {
        mSumDisplay = aSumDisplay;
    }


    public void actionMove(int aX, int aY)
    {
        if (mTree == null)
        {
            return;
        }

        ShapeContainsVisitor visitor = new ShapeContainsVisitor(new Point(aX, aY));

        boolean needsRedrawn = false;
        mTree.accept(visitor);

        if (visitor.isContained())
        {
            if (!mInside)
            {
                mInside = true;
                needsRedrawn = true;
                SumHighlightVisitor highlighter = new SumHighlightVisitor();
                mTree.accept(highlighter);

                if (highlighter.hasHightlightText())
                {
                    int start = highlighter.getHightlightStart();
                    int end = highlighter.getHightlightEnd();
                    mSumDisplay.setExtends(start, end);
                }
            }
        }
        else
        {
            if (mInside)
            {
                mInside = false;
                needsRedrawn = true;
                SumHighlightVisitor highlighter = new SumHighlightVisitor();
                mTree.accept(highlighter);
                mSumDisplay.clearExtents();
            }
        }

        if (needsRedrawn)
        {
            invalidate();
        }
    }

    public void actionDown(int aX, int aY, int aUnscaledX, int aUnscaledY)
    {
        ShapeContainsVisitor visitor = new ShapeContainsVisitor(new Point(aX, aY));

        if (mTree != null)
        {
            mTree.accept(visitor);
        }

        Shape clickedInsideShape = visitor.getResult();

        if (clickedInsideShape != null)
        {
            //Log.e("DrawView actionDown", "isContained");

            CompleteStatus completeStatus = TreeUtil.completeStatus(clickedInsideShape);
            if (completeStatus != CompleteStatus.IsComplete)
            {
                collapsedUnFinishedTree(completeStatus);
            }
            else
            {
              try {
                    clickedInsideShape.toggleCollapsed();

                    //   The collapsed size might be different
                    //   so need to work it out.
                    float textSize = TreeUtil.getTextFontSizeFromStyle(getContext());
                    resizeNodes(mTree, textSize, getScaledWidth());

                    SumHighlightVisitor highlight = new SumHighlightVisitor();
                    mTree.accept(highlight);
                    mSumDisplay.setText(highlight.getText());

                } catch (DivideByZeroException ex) {
                    visitor.getResult().toggleCollapsed();
                    //                        MessageDialog.openError(display.getActiveShell(),
                    //                                "Divide By Zero",
                    //                                "Cannot divide by zero.");
                    showDivideByZero();
                    //Log.e("div", "div problem ");
                }
                catch (InfinityException ex) {
                    visitor.getResult().toggleCollapsed();
                    showInfinity();
                    //Log.e("inf", "inf problem ");
                }
                invalidate();
            }
        }
        else
        {
            //  You had been inside but now clicked outside a shape,  redraw the thing.
            if (mInside)
            {
                mInside = false;
                invalidate();
            }
        }
    }

    /**
     * The collapsed size might be different
     * so need to work it out.
     * @param aTree tree to resize
     */
    public static void resizeNodes(Shape aTree, float aTextSize, int aWidth)
    {
        final Paint mBlackPaint = new Paint();
        mBlackPaint.setTextSize(aTextSize);
        mBlackPaint.setColor(Color.BLACK);

        NodeWidthVisitor widthVisitor = new NodeWidthVisitor(mBlackPaint);
        aTree.accept(widthVisitor);

        ShapesOnEachLevel overlap = new ShapesOnEachLevel();
        aTree.accept(overlap);

        Map<Integer, List<Shape>> shapesOnLevels = overlap.getLevelsMap();

        NodeOverlapPrevention prevention = new NodeOverlapPrevention(shapesOnLevels,
                                                                     widthVisitor,
                                                                     aWidth);
        prevention.adjustShapesOnEachLevel();

        aTree.accept(widthVisitor);
    }

    public void showDivideByZero()
    {
        showDialog("Divide By Zero",
                   "You Attempted to Divide by zero!");
    }
    public void showInfinity()
    {
        showDialog("Number too large",
                "The number is too large.");
    }

    private void showDialog(String aTitle, String aMessage)
    {
        new AlertDialog.Builder(getContext())
                .setTitle(aTitle)
                .setMessage(aMessage)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void collapsedUnFinishedTree(CompleteStatus aCompleteStatus)
    {
        String title = "Missing a number";
        String message = "This calculation is not valid";

        if (aCompleteStatus == CompleteStatus.MissingBrace)
        {
           title = "Missing Closing Bracket";
        }

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public String getExpression()
    {
        String currentExpression = "0";
        synchronized (mTree)
        {
            if (mTree != null)
            {
                SumHighlightVisitor highlight = new SumHighlightVisitor();
                mTree.accept(highlight);
                currentExpression = highlight.getText();
            }
        }
        return currentExpression;
    }


    public void setTreeOrientationQuery(TreeOrientationQuery aTreeOrientationQuery)
    {
        this.mTreeOrientationQuery = aTreeOrientationQuery;
    }

    public int getScaledWidth()
    {
        float width = getWidth();
        return (int) (width / mScaleFactor);
    }
}
