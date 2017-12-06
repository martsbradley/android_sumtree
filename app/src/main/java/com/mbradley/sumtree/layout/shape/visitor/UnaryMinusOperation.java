package com.mbradley.sumtree.layout.shape.visitor;

import com.mbradley.sumtree.layout.shape.Operation;


import java.math.BigDecimal;

public class UnaryMinusOperation extends Operation
{



    @Override
    public String getText()
    {
        return "-";
    }

	@Override
	public BigDecimal getValue()
	{
	    return null;
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
