package com.mbradley.sumtree.layout.shape;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class DivideOperationTest
{
    private DivideOperation mDivide;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Before
    public void beforeTests() 
    {
        mDivide = new DivideOperation();
    }

    @Test
    public void testDivideByZero()
    {
        thrown.expect(DivideByZeroException.class);
        BigDecimal left = new BigDecimal("2");
        BigDecimal right = new BigDecimal("0");
        mDivide.operate(left, right);
    }

    @Test
    public void testDivideByZeroAfterComputation()
    {
        thrown.expect(DivideByZeroException.class);

        BigDecimal left = new BigDecimal("9");
        BigDecimal right = new BigDecimal("3");
        BigDecimal result = mDivide.operate(left, right);

        BigDecimal shouldbeZero = result.subtract(new BigDecimal("3"));

        mDivide.operate(left,shouldbeZero );
    }

    @Test
    public void normal_division()
    {
        BigDecimal left = new BigDecimal("9");
        BigDecimal right = new BigDecimal("3"); ;


        BigDecimal result = mDivide.operate(left,right );
        int comparison = result.compareTo(new BigDecimal("3"));
        assertEquals(0, comparison);
    }
}
