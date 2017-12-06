package com.mbradley.sumtree.layout.shape;

import java.math.BigDecimal;
public class PowerOperation extends Operation
{

    @Override 
    public String getText()
    {
        if (isCollapsed())
        {
            return super.getText();
        }
        return "^";
    }

    @Override
    public BigDecimal operate(BigDecimal left, BigDecimal right)
    {
        BigDecimal bigD;
        try
        {
            double result = Math.pow(left.doubleValue(),right.doubleValue());
            bigD =  new BigDecimal(result);
        }
        catch (java.lang.NumberFormatException e)
        {
            throw new InfinityException();
        }
        return bigD;
    }
}