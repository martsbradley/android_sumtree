package com.mbradley.sumtree.layout.shape;

import java.math.BigDecimal;

public class DivideOperation extends Operation
{
    @Override
    public String getText()
    {
        if (isCollapsed())
        {
            return super.getText();
        }
        return "÷";
    }
    @Override
    public BigDecimal operate(BigDecimal left, BigDecimal right)
    {
//        BigDecimal myZero = BigDecimal.ZERO;
//        myZero = myZero.setScale(10);

       // if (myZero.equals(right)  || BigDecimal.ZERO.equals(right) )

/*
http://www.opentaps.org/docs/index.php/How_to_Use_Java_BigDecimal:_A_Tutorial
Comparison
It is important to never use the .equals() method to compare BigDecimals.
 That is because this equals function will compare the scale. If the scale is different,
 .equals() will return false, even if they are the same number mathematically.

 */
            //signum? on big decimal

        if (right.signum() == 0)
        {
            throw new DivideByZeroException();
        }

        BigDecimal result =  left.divide(right,10, BigDecimal.ROUND_HALF_DOWN);

        return result;
    }
}