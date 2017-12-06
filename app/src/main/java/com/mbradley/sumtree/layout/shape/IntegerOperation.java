package com.mbradley.sumtree.layout.shape;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class IntegerOperation extends Operation
{
    private final BigDecimal mValue;
    private boolean mEndsWithDecimal = false;

    public IntegerOperation(String aValue) 
    {
        if (StringUtils.endsWith(aValue, "."))
        {
            mEndsWithDecimal = true;
            aValue = StringUtils.removeEnd(aValue, ".");
        }
        mValue = new BigDecimal(aValue);
    }

    @Override
    public String getText()
    {
        String result;// = String.valueOf(mValue);
        result = mValue.toPlainString();
//        NumberFormat formatter = new DecimalFormat("0.######E0");
//        result = formatter.format(mValue.doubleValue());
        if (mEndsWithDecimal)
        {
            result = result + ".";
        }
        return result;
    }

	@Override
	public BigDecimal getValue()
	{
	    return mValue;
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
