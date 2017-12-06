package com.mbradley.sumtree.model.key;

import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.CompleteStatus;


public class ParseResult
{

    private final boolean mUpdated;
    private final boolean mZeroDivisionAttempt;
    private final boolean mInfinity;


    private final CompleteStatus mCompleteStatus;
    private final Shape mShape;
    private final int mWidth;
    private final int mHeight;
    private final String mExpression;

    private ParseResult(Builder aBuilder)
    {
        mShape = aBuilder.mShape;
        mWidth = aBuilder.mWidth;
        mHeight = aBuilder.mHeight;
        mExpression = aBuilder.mExpression;
        mUpdated = aBuilder.mUpdated;
        mZeroDivisionAttempt = aBuilder.mZeroDivisionAttempt;
        mInfinity = aBuilder.mInfinity;
        mCompleteStatus = aBuilder.mCompleteStatus;
    }


    private void checkState()
    {
        if (!mUpdated) throw new IllegalStateException();
    }

    public String getExpression()
    {
        checkState();
        return mExpression;
    }

    public boolean isUpdated()
    {
        return mUpdated;
    }

    public Shape getShape()
    {
        checkState();
        return mShape;
    }

    public int getWidth()
    {
        checkState();
        return mWidth;
    }

    public int getHeight()
    {
        checkState();
        return mHeight;
    }

    public boolean wasZeroDivisionAttempt()
    {
        return mZeroDivisionAttempt;
    }

    public boolean infinity()
    {
        return mInfinity;
    }

    public boolean unFinishedTree()
    {
        return mCompleteStatus == CompleteStatus.MissingNumber ||
               mCompleteStatus == CompleteStatus.MissingBrace;

    }

    public CompleteStatus getCompleteStatus()
    {
        return mCompleteStatus;
    }

    public static class Builder
    {
        private Shape mShape;
        private int mWidth = -1;
        private int mHeight = -1;
        private String mExpression;
        private boolean mUpdated = true;// By default assume it is updated unless built via
                                        // noUpdates method.
        private boolean mZeroDivisionAttempt = false;
        private boolean mInfinity = false;
        private CompleteStatus mCompleteStatus = CompleteStatus.IsComplete;
        public Builder shape(Shape aShape)
        {
            this.mShape = aShape;
            return this;
        }
        public Builder width(int aWidth)
        {
            this.mWidth = aWidth;
            return this;
        }
        public Builder height(int aHeight)
        {
            this.mHeight = aHeight;
            return this;
        }
        public Builder expression(String aExpression)
        {
            this.mExpression= aExpression;
            return this;
        }

        public Builder noUpdates()
        {
            this.mUpdated = false;
            return this;
        }
        public Builder zeroDivisionAttempted()
        {
            this.mZeroDivisionAttempt = true;
            noUpdates();
            return this;
        }

        public Builder infinity()
        {
            this.mInfinity = true;
            noUpdates();
            return this;
        }

        public Builder closingBraceMissing()
        {
            this.mCompleteStatus = CompleteStatus.MissingBrace;
            noUpdates();
            return this;
        }
        public Builder finalNumberMissing()
        {
            this.mCompleteStatus = CompleteStatus.MissingNumber;
            noUpdates();
            return this;
        }
        public ParseResult build()
        {
            return new ParseResult(this);
        }
    }
}

