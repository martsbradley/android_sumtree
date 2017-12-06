package com.mbradley.sumtree.sumdisplay;

public class Highlighted extends HighlightStrategyImpl
{
    public Highlighted(String aEntireText, int aStart, int aEnd) 
    {
        super(aEntireText, aStart, aEnd);
    }

    public String getBeginning()
    {
        String textNotHlStart = getText().substring(0, getStartIdx());
        return textNotHlStart;
    }

    @Override
    public String getMiddle()
    {
        String textNotHlStart = getText().substring(getStartIdx(), getEndIdx());
        return textNotHlStart;
    }

    @Override
    public String getEnd()
    {
        return getText().substring(getEndIdx());
    }
}
