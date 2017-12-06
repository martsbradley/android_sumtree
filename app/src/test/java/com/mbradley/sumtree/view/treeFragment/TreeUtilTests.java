package com.mbradley.sumtree.view.treeFragment;

import com.mbradley.sumtree.layout.shape.IntegerOperation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by martin on 10/09/16.
 */
public class TreeUtilTests
{
    @Test
    public void normal_number_0()
    {
        normal_number("0");
    }
    @Test
    public void normal_number_10()
    {
        normal_number("10");
    }
    @Test
    public void normal_number_100()
    {
        normal_number("100");
    }

    @Test
    public void normal_number_25point1()
    {
        normal_number("25.1");
    }

    @Test
    public void massiveNumber()
    {
        IntegerOperation op = new IntegerOperation("1100000000000");
        assertEquals("1100000000000",TreeUtil.getDrawnText(op));
    }

    private void normal_number(String aNumber)
    {
        IntegerOperation op = new IntegerOperation(aNumber);
        assertEquals(aNumber,TreeUtil.getDrawnText(op));
    }
}
