package com.mbradley.sumtree.sumdisplay;

public class NotHighlighted extends HighlightStrategyImpl
{
    public NotHighlighted(String aEntireText) 
    {
        super(aEntireText);
    }

    @Override
    public String getBeginning()
    {
        return getText();
    }

    @Override
    public String getMiddle()
    {
        return "";
    }

    @Override
    public String getEnd()
    {
        return "";
    }
}