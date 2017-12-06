package com.mbradley.sumtree.layout.shape.visitor;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.parser.IncompleteSumParser;
import com.mbradley.sumtree.view.CalculatorActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class NodeWidthVisitorEmulatedTest
{
    @Rule
    public ActivityTestRule<CalculatorActivity> mActivityRule = new ActivityTestRule(CalculatorActivity.class);



    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"(1)", true},
                {"(1+2)", true},
                {"1+2+3", true},
                {"(1+2)*3", true},
                {"3*(1+2)", true},
                {"(1", true},
                {"1+", true},
                {"9+6+(", true},
                {"1*(", true},

        });
    }

    @Test
    public void runTests() throws IOException
    {
        for (Object[] vals : data())
        {
            String input = (String)vals[0];
            boolean success = (Boolean)vals[1];
            runTests(input, success);
        }
    }

    public void runTests(String aInput, boolean aResult) throws IOException
    {
        IncompleteSumParser parser = new IncompleteSumParser(aInput);
        Shape shape = parser.parse();

        PopulateBlankShapeVisitor populator = new PopulateBlankShapeVisitor();
        shape.accept(populator);
        assertNotNull(shape);

        final Paint mBlackPaint = new Paint();
        Typeface bold = Typeface.create(Typeface.SERIF, Typeface.BOLD);
        mBlackPaint.setTypeface(bold);
        mBlackPaint.setTextSize(60);
        mBlackPaint.setColor(Color.BLACK);
        NodeWidthVisitor width = new NodeWidthVisitor(mBlackPaint);
        shape.accept(width);
    }
}
