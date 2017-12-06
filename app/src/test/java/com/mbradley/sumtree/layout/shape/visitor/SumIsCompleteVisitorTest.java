package com.mbradley.sumtree.layout.shape.visitor;

import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.parser.IncompleteSumParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



@RunWith(Parameterized.class)
public class SumIsCompleteVisitorTest
{
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"(1)",     CompleteStatus.IsComplete},
                {"(1+2)",   CompleteStatus.IsComplete},
                {"1+2+3",   CompleteStatus.IsComplete},
                {"(1+2)*3", CompleteStatus.IsComplete},
                {"3*(1+2)", CompleteStatus.IsComplete},
                {"10÷(9÷3-3)", CompleteStatus.IsComplete},
                {"1+",      CompleteStatus.MissingNumber},
                {"(1+2)*",  CompleteStatus.MissingNumber},
                {"(1",      CompleteStatus.MissingBrace},
                {"9+6+(",   CompleteStatus.MissingBrace},
                {"1*(",     CompleteStatus.MissingBrace},
        });
    }

    private String mInput;
    private CompleteStatus mStatus;

    public SumIsCompleteVisitorTest(String aInput, CompleteStatus aStatus)
    {
        this.mInput = aInput;
        this.mStatus = aStatus;
    }

    @Test
    public void runTests() throws IOException
    {
        IncompleteSumParser parser = new IncompleteSumParser(mInput);
        Shape shape = parser.parse();

        PopulateBlankShapeVisitor populator = new PopulateBlankShapeVisitor();
        shape.accept(populator);

        SumIsCompleteVisitor completeVisitor = new SumIsCompleteVisitor();
        shape.accept(completeVisitor);

        assertEquals(mStatus, completeVisitor.sumIsComplete());
    }
}
