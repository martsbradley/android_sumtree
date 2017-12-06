package com.mbradley.sumtree.layout.shape;

import java.math.BigDecimal;

public class MultiplyOperation extends Operation
{

    @Override 
    public String getText()
    {
        if (isCollapsed())
        {
            return super.getText();
        }
        return "*";
    }
    @Override
    public BigDecimal operate(BigDecimal left, BigDecimal right)
    {
        return left.multiply(right);
    }
}
