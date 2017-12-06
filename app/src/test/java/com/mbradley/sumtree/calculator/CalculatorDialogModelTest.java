package com.mbradley.sumtree.calculator;

import com.mbradley.sumtree.view.calculator.CalculatorDialogModel;
import com.mbradley.sumtree.model.key.KeyPressOutcome;
import com.mbradley.sumtree.model.key.KeyValue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;
public class CalculatorDialogModelTest
{
    private CalculatorDialogModel model;
    
    @Before
    public void beforeTests() throws IOException
    {
        model = new CalculatorDialogModel();
    }
    @Test
    public void oneInteger()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.TWO);

        assertTrue(result.expressionUpdated());
        assertEquals("2", result.getExpression());
    }
    @Test
    public void backToReset()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.TWO);

        assertTrue(result.expressionUpdated());
        assertEquals("2", result.getExpression());

        result = model.keyPressed(KeyValue.BACK);
        assertTrue(result.expressionUpdated());
        assertEquals("0", result.getExpression());

        result = model.keyPressed(KeyValue.TWO);

        assertTrue(result.expressionUpdated());
        assertEquals("2", result.getExpression());
    }

    @Test
    public void backBigInteger()
    {
        model.keyPressed(KeyValue.TWO);
        KeyPressOutcome result = model.keyPressed(KeyValue.TWO);

        assertTrue(result.expressionUpdated());
        assertEquals("22", result.getExpression());
    }
    @Test
    public void backSmallDecimal()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.TWO);

        model.setPreviousExpression(result.getExpression());
        result =  model.keyPressed(KeyValue.DOT);

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.FIVE);

        assertTrue(result.expressionUpdated());
        assertEquals("2.5", result.getExpression());
    }
    @Test
    public void decimal_without_leading_number()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.DOT);
        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.FIVE);

        assertTrue(result.expressionUpdated());
        assertEquals("0.5", result.getExpression());
    }
    @Test
    public void testNumberAfterResetStateRemovesTheZero()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.TWO);
        assertEquals("2", result.getExpression());

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.BACK);

        assertEquals("0", result.getExpression());

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.NINE);

        assertEquals("9", result.getExpression());

        model.setPreviousExpression(result.getExpression());

        result = model.keyPressed(KeyValue.MULTIPLY);

        assertEquals("9*", result.getExpression());
    }

    @Test
    public void testZeroAfterResetStateStaysZero()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.TWO);
        assertEquals("2", result.getExpression());

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.BACK);

        assertEquals("0", result.getExpression());

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.ZERO);

        assertTrue(result.expressionUpdated());

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.MULTIPLY);

        assertEquals("0*", result.getExpression());

        result = model.keyPressed(KeyValue.FIVE);

        assertEquals("0*5", result.getExpression());
    }

    @Test
    public void testEqualsOnNumber()
    {
        model.keyPressed(KeyValue.TWO);
        KeyPressOutcome result = model.keyPressed(KeyValue.EQUALS);

        assertEquals("2", result.getExpression());
        assertTrue(result.equalsWasPressed());
    }

    @Test
    public void testEqualsOnReset()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.EQUALS);

        assertEquals("0", result.getExpression());
        assertTrue(result.equalsWasPressed());
    }

    @Test
    public void testMultiplyAfterDivide()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.NINE);

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.DIVIDE);

        assertEquals("9÷", result.getExpression());

        model.setPreviousExpression(result.getExpression());

        result = model.keyPressed(KeyValue.MULTIPLY);
        assertFalse(result.expressionUpdated());
    }
    @Test
    public void testDivideAfterMultiply()
    {
        KeyPressOutcome result = model.keyPressed(KeyValue.NINE);

        model.setPreviousExpression(result.getExpression());
        result = model.keyPressed(KeyValue.MULTIPLY);

        assertEquals("9*", result.getExpression());

        model.setPreviousExpression(result.getExpression());

        result = model.keyPressed(KeyValue.DIVIDE);
        assertFalse(result.expressionUpdated());
    }
}
