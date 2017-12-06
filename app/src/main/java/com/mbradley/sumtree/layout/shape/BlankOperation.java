package com.mbradley.sumtree.layout.shape;

import java.math.BigDecimal;

public class BlankOperation extends Operation
{

    public BlankOperation()
    {
    }

    @Override
    public String getText()
    {
        return "";
    }

    @Override
    public BigDecimal getValue()
    {
        return new BigDecimal("0");
    }

    @Override
    public BigDecimal operate(BigDecimal left, BigDecimal right)
    {
        // This is bad design, integers should not
        // have the operate method.
        return null;
    }
    @Override
    public boolean isCollapsed()
    {
        return false;// cannot collapse integers.
    }
}
