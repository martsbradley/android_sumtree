package com.mbradley.sumtree.layout.shape;

import java.math.BigDecimal;

public class BraceOperation extends Operation
{
    public String getText()
    {
        if (isCollapsed())
        {
            return super.getText();
        }
        return "(";
    }
    
    @Override
    public BigDecimal operate(BigDecimal left, BigDecimal right)
    {
        return null;//getLeftChild().operate( left,  right);
    }

    @Override
    public BigDecimal getValue()
    {
        BigDecimal result = null;
        if (getLeftChild() != null /*&& mRightChild != null*/)
        {
            result = getLeftChild().getValue();
        }
        return result;
    }
}