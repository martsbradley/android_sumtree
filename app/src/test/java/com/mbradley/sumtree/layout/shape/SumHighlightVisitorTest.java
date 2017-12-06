package com.mbradley.sumtree.layout.shape;

import com.mbradley.sumtree.parser.IncompleteSumParser;
import com.mbradley.sumtree.layout.shape.visitor.PopulateBlankShapeVisitor;
import com.mbradley.sumtree.layout.shape.visitor.SumHighlightVisitor;

//import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class SumHighlightVisitorTest
{
    private String mInput;
    private boolean mResult;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "(1)",            true },
                { "(1+2)",          true },
                { "1+2+3",          true },
                { "(1+2)*3",        true },
                { "3*(1+2)",        true },
                { "(1",             true },
                { "1+",             true },
                { "9+6+(",          true },
                { "1*(",            true },
                { "0.0000000002",   true },
        });
    }

    //private ParseTree parseTree;

    public SumHighlightVisitorTest(String aInput, boolean aResult)
    {
        mInput = aInput;
        mResult = !aResult;
    }
    
    @Test
    public void runTests() throws IOException
    {
        IncompleteSumParser parser = new IncompleteSumParser(mInput);
        Shape shape = parser.parse();

        PopulateBlankShapeVisitor populator = new PopulateBlankShapeVisitor();
        shape.accept(populator);
        assertNotNull(shape);

        SumHighlightVisitor textVisitor = new SumHighlightVisitor();
        shape.accept(textVisitor);

        String text = textVisitor.getText();

        assertEquals(mInput, text);
    }
}
