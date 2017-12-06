package com.mbradley.sumtree.layout.shape;

import android.util.Log;

import com.mbradley.sumtree.layout.Rank;
import com.mbradley.sumtree.layout.shape.visitor.Visitor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

//import org.eclipse.swt.graphics.Point;

public abstract class Operation implements Shape
{
    
    /*getPosition will return the centre position
      of the box.
    
    mEdgeXPosition will return the left most point of the
    shape
    */
    
    private boolean mMouseOver = false;


    private static final int BORDERWIDTH = 10;
    private int mTextWidth;
    private Shape mParent;
    private Shape mLeftChild;
    private Shape mRightChild;
    private int mEdgeXPosition;
    private int mEdgeYPosition;
    private boolean mCollapsed = false;
    private boolean mHasOpenBrace = false;
    private boolean mHasCloseBrace = false;
    private boolean mShowsAsScientific = false;
    @Override
    public boolean contains(Point aPoint)
    {
        boolean result = aPoint.x > mEdgeXPosition && 
                         aPoint.x < (mEdgeXPosition + getWidth()) &&
                         aPoint.y > mEdgeYPosition && 
                         aPoint.y < (mEdgeYPosition + Rank.RANK_HEIGHT);
         return result;
    }

    @Override
    public Shape getParent()
    {
        return mParent;
    }

    public void setParent(Shape aParent)
    {
        this.mParent = aParent;
    }

    @Override
    public Shape getLeftChild()
    {
        return mLeftChild;
    }

    @Override
    public Shape getRightChild()
    {
        return mRightChild;
    }

    public boolean isParent()
    {
        return !mCollapsed && (mLeftChild != null || mRightChild != null);
    }

    public Point getLeftChildsPosition()
    {
       //assert isParent();
       return mLeftChild.getPosition();
    }
        
    public Point getRightChildsPosition()
    {
       //assert isParent();
       return mRightChild.getPosition();
    }
    
    public Point getPosition()
    {
        return new Point(mEdgeXPosition + mTextWidth/2, 
                         mEdgeYPosition + Rank.RANK_HEIGHT /2);
    }

    @Override
    public void setXPosition(int aXPosition)
    {
        this.mEdgeXPosition = aXPosition;
    }

    @Override
    public int getEdgeXPosition()
    {
        return this.mEdgeXPosition;
    }

    /*
     * Return the entire width of the shape
     *
     */
    @Override
    public int getWidth()
    {
        int naturalWidth= mTextWidth + 2*BORDERWIDTH;
        //  Max to make the shapes look square when too small..
        return Math.max(naturalWidth, Rank.RANK_HEIGHT);
    }

    @Override
    public void setEdgeYPosition(int aEdgeYPosition)
    {
        this.mEdgeYPosition = aEdgeYPosition;
        
    }
    @Override
    public int getEdgeYPosition()
    {
        return this.mEdgeYPosition;
    }

    @Override
    public void setChild(Shape aShape)
    {
        if (aShape != null)
        {
            aShape.setParent(this);
        }

        if (mLeftChild == null)
            mLeftChild = aShape;
        else if (mRightChild == null)
            mRightChild = aShape;
        else
            throw new IllegalStateException();
    }

    @Override
    public void accept(Visitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public void setMouseOver(boolean aMouseOver)
    {
        mMouseOver = aMouseOver;
    }

    @Override
    public boolean containsMouse()
    {
    	return mMouseOver;
    }
    
    public void setTextWidth(int aTextWidth)
    {
        mTextWidth = aTextWidth;
    }

    private int getCenter()
    {
		return mEdgeXPosition+ getWidth()/2;
    }
	@Override
	public Point getTopCenter() 
	{
		return new Point(getCenter(), mEdgeYPosition);
	}

	@Override
	public Point getBottomCenter() 
	{
		return new Point(getCenter(), mEdgeYPosition+ Rank.RANK_HEIGHT);
	}
	@Override
	public void toggleCollapsed()
	{
	    this.mCollapsed = !mCollapsed;
        setShowsAsScientific(false);

        if (isParent())
        {
            int total = getLeftChild().getEdgeXPosition() + getRightChild().getEdgeXPosition();
            setXPosition(total/2);
        }
	}

	public boolean isCollapsed()
	{
	    return mCollapsed;
	}

	@Override
	public BigDecimal getValue()
	{
	    BigDecimal result = null;
        if (mLeftChild != null && mRightChild != null)
	    {
	        BigDecimal left = getLeftChild().getValue();
	        BigDecimal right = getRightChild().getValue();
	        result = operate(left, right);
	    }
	    return result;
	}

    //public BigDecimal operate(BigDecimal left, BigDecimal right)
    //{
        //return new BigDecimal("-99");
    //}
    @Override 
    public String getText()
    {
        BigDecimal value = getValue();
        DecimalFormat df = new DecimalFormat();

        df.setMaximumFractionDigits(6);

        df.setMinimumFractionDigits(0);

        df.setGroupingUsed(false);

        String result = df.format(value);
        return  result;
    }

    public boolean hasOpenBrace()
    {
        return mHasOpenBrace;
    }
    public boolean hasCloseBrace()
    {
        return mHasCloseBrace;
    }
    public void setOpenBrace(boolean aValue)
    {
        this.mHasOpenBrace = aValue;
    }
    public void setCloseBrace(boolean aValue)
    {
        this.mHasCloseBrace = aValue;
    }
    public void setShowsAsScientific(boolean aShowsAsScientific)
    {
        mShowsAsScientific = aShowsAsScientific;
    }

    public boolean getShowsAsScientific()
    {
        return mShowsAsScientific;
    }

}