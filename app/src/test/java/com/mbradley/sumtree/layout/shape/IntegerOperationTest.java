package com.mbradley.sumtree.layout.shape;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class IntegerOperationTest
{
    @Before
    public void beforeTests() throws IOException
    {

    }
    @Test
    public void testTingy()
    {
        IntegerOperation tiny = new IntegerOperation("0");
        assertEquals("0", tiny.getText());
    }
    @Test
    public void testDecimal()
    {
        IntegerOperation tiny = new IntegerOperation("0.");
        assertEquals("0.", tiny.getText());
        assertEquals(new BigDecimal("0"), tiny.getValue());
    }
}
