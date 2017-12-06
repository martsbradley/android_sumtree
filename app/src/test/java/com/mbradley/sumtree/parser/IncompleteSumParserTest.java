package com.mbradley.sumtree.parser;


import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.PopulateBlankShapeVisitor;
import com.mbradley.sumtree.layout.shape.visitor.SumHighlightVisitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class IncompleteSumParserTest
{

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {"1",            true,  "1"         , new BigDecimal("1")    },
                {"1+2",          true,  "1+2"       , new BigDecimal("3")    },
                {"1+2+3",        true,  "1+2+3"     , new BigDecimal("6")    },
                {"1+2*3",        true,  "1+2*3"     , new BigDecimal("7")    },
                {"1*2*3",        true,  "1*2*3"     , new BigDecimal("6")    },
                {"1*2+3",        true,  "1*2+3"     , new BigDecimal("5")    },
                {"1-2-3",        true,  "1-2-3"     , new BigDecimal("-4")   },
                {"(1)",          true,  "(1)"       , new BigDecimal("1")    },
                {"(1+2)",        true,  "(1+2)"     , new BigDecimal("3")    },
                {"(1+2*3)",      true,  "(1+2*3)"   , new BigDecimal("7")    },
                {"((1+2)*3",     true,  "((1+2)*3"  , new BigDecimal("9")    },//10
                {"(2)(3)",       true,  "(2)*(3)"   , new BigDecimal("6")    },
                {"2(3)",         true,  "2*(3)"     , new BigDecimal("6")    },
                { "1+2*(3)",     true,  "1+2*(3)"   , new BigDecimal("7")    },
                { "1+2*(3+3)",   true,  "1+2*(3+3)" , new BigDecimal("13")   },
                { "22",          true,  "22"        , new BigDecimal("22")   },
                { "22.",         true,  "22."       , new BigDecimal("22")   },
                { "22.32",       true,  "22.32"     , new BigDecimal("22.32")},
                { "-1",          true,  "-1"        , new BigDecimal("-1")   },
                { "100÷20÷5",    true,  "100÷20÷5"  , new BigDecimal("1")    },//19
                {"1+2*",         true,  "1+2*"      , null                   },
                {"1+",           true,  "1+"        , null                   },
                {"1*",           true,  "1*"        , null                   },
                {"(1",           true,  "(1"        , null                   },
                {"2(3",          true,  "2*(3"      , null                   },
                {"2(",           true,  "2*("       , null                   },
                { "1+2*(",       true,  "1+2*("     , null                   },
                { "1+2*(3+",     true,  "1+2*(3+"   , null                   },
                { "1+2*(3+3",    true,  "1+2*(3+3"  , null                   },
                { "(3+3)+1+2*",  true,  "(3+3)+1+2*", null                   },
                { "-",           true,   "-"        , null                   },
                { "1*(",         true,  "1*("       , null                   },
                { "1+2*(3+w",    false, ""          , null                   },
                {"1+)2*3(",      false, ""          , null                   },
                { "what t",      false, ""          , null                   },
                { "*3",          false,  ""         , null                   },
                { "2+(*",        false,  ""         , null                   },
                { "+",           false,  ""         , null                   },
                { "+3",          false,  ""         , null                   },
                { "(9*)",        false,  ""         , null                   },
                { "2^3",         true,   "2^3"      , new BigDecimal("8")    },//40
                { "2^(1+1)",     true,   "2^(1+1)"  , new BigDecimal("4")    },//40
        });
    }

    private String mInput;
    private boolean mValidExpr;//  Is the sum even a valid expression
    private String mExpected;
    private BigDecimal mResult;// null when the sum is incomplete


    public IncompleteSumParserTest(String aInput, boolean aValidExpr, String aExpected, BigDecimal aResult)
    {
        this.mInput = aInput;

        this.mValidExpr = aValidExpr;
        this.mExpected = aExpected;
        this.mResult = aResult;
    }

    @Test
    public void t()
    {
        IncompleteSumParser parser = new IncompleteSumParser(mInput);

        Shape shape = null;

        try {
            shape = parser.parse();
        }
        catch (RuntimeException e)
        {
            assertFalse(mValidExpr);
            return;
        }

        assertTrue(mValidExpr);

        PopulateBlankShapeVisitor populator = new PopulateBlankShapeVisitor();
        shape.accept(populator);

        SumHighlightVisitor textVisitor = new SumHighlightVisitor();
        shape.accept(textVisitor);

        String text = textVisitor.getText();

        text.length();
        assertEquals(mExpected, text);

        if (mResult != null)
        {
            shape.toggleCollapsed();
            BigDecimal value = shape.getValue();

            assertEquals(0, value.compareTo(mResult));
        }
    }
}