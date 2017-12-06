package com.mbradley.sumtree.sumdisplay;

abstract class HighlightStrategyImpl implements HighlightStrategy
{
    private final String mEntireText;
    private int mStartIdx = -1;
    private int mEndIdx = -1;

    HighlightStrategyImpl(String aEntireText)
    {
        this.mEntireText = aEntireText;
    }

    HighlightStrategyImpl(String aEntireText, int aStart, int aEnd)
    {
        this(aEntireText);
        this.mStartIdx = aStart;
        this.mEndIdx = aEnd;
    }

    String getText()
    {
        return mEntireText;
    }

    int getStartIdx()
    {
        return mStartIdx;
    }

    int getEndIdx()
    {
        return mEndIdx;
    }
    
}
