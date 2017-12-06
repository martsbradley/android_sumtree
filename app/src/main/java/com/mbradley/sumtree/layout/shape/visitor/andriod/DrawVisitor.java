package com.mbradley.sumtree.layout.shape.visitor.andriod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.mbradley.sumtree.layout.EOrientation;
import com.mbradley.sumtree.layout.Rank;
import com.mbradley.sumtree.layout.shape.BlankOperation;
import com.mbradley.sumtree.layout.shape.Point;
import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.Visitor;
import com.mbradley.sumtree.view.treeFragment.TreeUtil;

public class DrawVisitor implements Visitor
{
    private static final int STROKE_WIDTH = 4;
    private Canvas mCanvas;
    private EOrientation mOrientation;
    private final Paint mBlackPaint = new Paint();
    private final Paint mBlackLinePaint = new Paint();
    private final Paint mShapeOutlineLinePaint = new Paint();
    private final Paint mYellowPaint = new Paint();
    private final Paint mSilver = new Paint();
    private final Paint mDarkOrange = new Paint();
    private final Paint mLime = new Paint();
    private final Paint mAquamarine = new Paint();
    private final Paint mCadetBlue = new Paint();



    public DrawVisitor(Context context)
    {
        int textSize = TreeUtil.getTextFontSizeFromStyle(context);

        //Log.e("textSize", textSize +" ");

        mBlackPaint.setTextSize(textSize);
        mBlackPaint.setColor(Color.BLACK);

        mBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBlackPaint.setStrokeWidth(1);

        mBlackLinePaint.setColor(Color.BLACK);
        mBlackLinePaint.setStyle(Paint.Style.STROKE);
        mBlackLinePaint.setStrokeWidth(STROKE_WIDTH);

        mShapeOutlineLinePaint.setColor(Color.RED);
        mShapeOutlineLinePaint.setStyle(Paint.Style.STROKE);
        mShapeOutlineLinePaint.setStrokeWidth(STROKE_WIDTH);

        mYellowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mYellowPaint.setColor(Color.YELLOW);

        mSilver.setColor(Color.parseColor("#C0C0C0"));
        mSilver.setStyle(Paint.Style.FILL_AND_STROKE);
        mSilver.setStrokeWidth(STROKE_WIDTH);

        mDarkOrange.setColor(Color.parseColor("#ff8c00"));
        mDarkOrange.setStyle(Paint.Style.FILL_AND_STROKE);
        mDarkOrange.setStrokeWidth(STROKE_WIDTH);

        mLime.setColor(Color.parseColor("#00FF00"));
        mLime.setStyle(Paint.Style.FILL_AND_STROKE);
        mLime.setStrokeWidth(STROKE_WIDTH);

        mAquamarine.setColor(Color.parseColor("#7FFFD4"));
        mAquamarine.setStyle(Paint.Style.FILL_AND_STROKE);
        mAquamarine.setStrokeWidth(STROKE_WIDTH);

        mCadetBlue.setColor(Color.parseColor("#5f9ea0"));
        mCadetBlue.setStyle(Paint.Style.FILL_AND_STROKE);
        mCadetBlue.setStrokeWidth(STROKE_WIDTH);

    }


    @Override
    public void visit(Shape aOp)
    {
    	operate(aOp);
    	if (aOp.isParent())
    	{
    		visit(aOp.getLeftChild());
    		visit(aOp.getRightChild());
    	}
    }

    private void operate(Shape op) 
    {
        if (op instanceof BlankOperation) return;





        drawLine(op, mBlackLinePaint);

        if (op.containsMouse())
        {
            mShapeOutlineLinePaint.setColor(Color.LTGRAY);
        }
        else
        {
            mShapeOutlineLinePaint.setColor(Color.BLACK);
        }

        String drawnText = TreeUtil.getDrawnText(op);
        if (TreeUtil.isCollapsedCalculation(op))
        {
            drawRectangle(op, mCadetBlue);
        }
        else if (TreeUtil.isCalculation(op))
        {
            drawRectangle(op, getCalculationPaint(op));
        }
        else
        {
            drawOval(op, mYellowPaint);
        }

        drawText(op, mBlackPaint, drawnText);
    }

    public void drawGrid()
    {
        int width = mCanvas.getWidth();
        int height = mCanvas.getHeight();

        int deciWidth = width/10;
        int deciHeight = height/10;

        for (int x = deciWidth; x < width; x += deciWidth)
        {
            mCanvas.drawLine(x, 0, x, height, mBlackLinePaint);
        }
        for (int y = deciHeight; y < height; y += deciHeight)
        {
            mCanvas.drawLine(0, y, width, y, mBlackLinePaint);
        }
    }

    private Paint getCalculationPaint(Shape op)
    {
        String text = op.getText();
        Paint paint= mYellowPaint;

        if ("+".equals(text))
        {
            paint = mAquamarine;
        }
        else if ("-".equals(text))
        {
            paint = mSilver;
        }
        else if ("*".equals(text))
        {
            paint = mDarkOrange;
        }
        else if ("÷".equals(text))
        {
            paint = mLime;
        }
        return paint;
    }

    private void drawText(Shape op, Paint aPaint, String aText)
    {
        int textWidth = (int) aPaint.measureText(aText);

        Rect bounds = new Rect();
        aPaint.getTextBounds(aText, 0, aText.length(), bounds);
        int textHeight = bounds.bottom - bounds.top;

        int textVerticalOffset = Rank.RANK_HEIGHT /2 +textHeight/2;

        int x =  op.getEdgeXPosition()+(op.getWidth()-textWidth)/2;

        int y = op.getEdgeYPosition() + textVerticalOffset;

        mCanvas.drawText(aText,
                         x,
                         y,
                         aPaint);
    }

    private void drawRectangle(Shape op, Paint aPaint)
    {

        RectF r = new RectF(op.getEdgeXPosition(),
                op.getEdgeYPosition(),
                op.getEdgeXPosition() + op.getWidth(),
                op.getEdgeYPosition() +  Rank.RANK_HEIGHT);

        mCanvas.drawRect(r,
                    aPaint);

        mCanvas.drawRect(r,
                    mShapeOutlineLinePaint);

    }
    private void drawOval(Shape op, Paint aPaint)
    {
        RectF r = new RectF(op.getEdgeXPosition(),
                op.getEdgeYPosition(),
                op.getEdgeXPosition() +op.getWidth(),
                op.getEdgeYPosition() + Rank.RANK_HEIGHT);

     //   Log.w("Oval",  "Width" + op.getWidth());

        mCanvas.drawOval(r,
                aPaint);
        
        mCanvas.drawOval(r,
                mShapeOutlineLinePaint);
    }
    private void drawLine(Shape op, Paint aPaint)
    {
        if (mOrientation == EOrientation.ETreeDown &&
                op.getParent() != null)
        {
            Point top = op.getTopCenter();

            Shape daddy = op.getParent();

            Point bot = daddy.getBottomCenter();
            mCanvas.drawLine(top.x, top.y, bot.x, bot.y, aPaint);
        }
        if (mOrientation == EOrientation.ETreeUp &&
                op.getParent() != null)
        {
            Point top = op.getBottomCenter();

            Shape daddy = op.getParent();

            Point bot = daddy.getTopCenter();
            mCanvas.drawLine(top.x, top.y, bot.x, bot.y, aPaint);
        }
    }

    public void setCanvas(Canvas aCanvas)
    {
        this.mCanvas = aCanvas;
    }

    public void setOrientation(EOrientation aOrientation) {
        this.mOrientation = aOrientation;
    }
}
