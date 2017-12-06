package com.mbradley.sumtree.layout.shape.visitor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.mbradley.sumtree.layout.EOrientation;
import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.model.SumtreeParser;
import com.mbradley.sumtree.model.TreeLayout;
import com.mbradley.sumtree.model.key.ParseResult;
import com.mbradley.sumtree.view.CalculatorActivity;
import com.mbradley.sumtree.view.treeFragment.DrawView;
import com.mbradley.sumtree.view.treeFragment.TreeUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;


@RunWith(AndroidJUnit4.class)
public class OverlapPreventionVisitorTest
{
    private static final int SCREEN_WIDTH = 500;
    private Context mContext;

    @Rule
    public ActivityTestRule<CalculatorActivity> mActivityRule = new ActivityTestRule(CalculatorActivity.class);

    @Before
    public void dosetup() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    /**
     * After toggling the tree the nodes need to be recalculated
     *
     * @param aShape
     */
    private void resizeNodes(Shape aShape)
    {
        float textSize = TreeUtil.getTextFontSizeFromStyle(mContext);
        DrawView.resizeNodes(aShape, textSize, SCREEN_WIDTH);

        /*final Paint mBlackPaint = new Paint();
        mBlackPaint.setTextSize(textSize);
        mBlackPaint.setColor(Color.BLACK);

        NodeWidthVisitor width = new NodeWidthVisitor(mBlackPaint);
        aShape.accept(width);*/
    }

    /**
     * helper method to geta tree from a string.
     *
     * @param input
     * @return
     */
    private Shape getShape(String input) {
        TreeLayout treelayout = new TreeLayout(mContext,
                EOrientation.ETreeDown);

        SumtreeParser sumtreeParser = new SumtreeParser(treelayout);

        ParseResult parseResult = sumtreeParser.parse(input);

        Shape shape = parseResult.getShape();
        return shape;
    }

    @Test
    public void leftChildScientific() throws IOException
    {
        String input = "3^32+1";

        Shape shape = getShape(input);
        shape.getLeftChild().toggleCollapsed();

        resizeNodes(shape);

        boolean overlap = NodeOverlapPrevention.overlaps(shape.getLeftChild(), shape.getRightChild());

        assertEquals(false, overlap);
        assertEquals(true, shape.getLeftChild().getShowsAsScientific());
    }

    private Map<Integer, List<Shape>> shapesOnEachLevel(Shape aShape) {
        ShapesOnEachLevel computeLevels = new ShapesOnEachLevel();
        aShape.accept(computeLevels);
        return computeLevels.getLevelsMap();
    }

    private void testCollapseRightHandsideTree(String aInput, boolean aRHSTreeScientific)
    {
        Shape shape = getShape(aInput);
        shape.getRightChild().toggleCollapsed();

        resizeNodes(shape);

        Map<Integer, List<Shape>> shapesOnLevels = shapesOnEachLevel(shape);

        final Paint mBlackPaint = new Paint();
        float textSize = TreeUtil.getTextFontSizeFromStyle(mContext);
        mBlackPaint.setTextSize(textSize);
        mBlackPaint.setColor(Color.BLACK);

        NodeWidthVisitor widthVisitor = new NodeWidthVisitor(mBlackPaint);

        NodeOverlapPrevention prevention = new NodeOverlapPrevention(shapesOnLevels, widthVisitor, SCREEN_WIDTH);
        prevention.adjustShapesOnEachLevel();

        resizeNodes(shape);

        assertEquals(aRHSTreeScientific, shape.getRightChild().getShowsAsScientific());
        // should not be shown as scientific because there is room on the screen for the whole
        // number.
    }

    @Test
    public void notScientificWhenEnoughSpace()
    {
        String input = "1+3^12";

        // Should not be shown as scientific because there is
        // room on the screen for the whole number.
        boolean rHSTreeScientific = false;
        testCollapseRightHandsideTree(input, rHSTreeScientific);
    }

    @Test
    public void scientificWhenEnoughSpace()
    {
        String input = "1+3^55";

        // Should not be shown as scientific because there is
        // room on the screen for the whole number.
        boolean rHSTreeScientific = true;
        testCollapseRightHandsideTree(input, rHSTreeScientific);
    }


    @Test
    public void reAlignLeftNode()
    {
        //3^15+4 was the first operand of the + was collapsing into the second operand.
        // this is because its setting the scientific mode but even then it is too wide.
        // and overlaps the 4.

        String input = "3^15+4";

        Shape shape = getShape(input);
        shape.getLeftChild().toggleCollapsed();

        resizeNodes(shape);

        Shape leftChild = shape.getLeftChild();

        int leftShapeRightEdge = leftChild.getEdgeXPosition() + leftChild.getWidth();

        int leftEdgeeRightShape = shape.getRightChild().getEdgeXPosition();
        boolean ok = leftShapeRightEdge <= leftEdgeeRightShape;
        assertEquals(true, ok);
    }

    @Test
    public void collapseRoot_causing_issue()
    {
        //3^15+4 was the first operand of the + was collapsing into the second operand.
        // this is because its setting the scientific mode but even then it is too wide.
        // and overlaps the 4.

        String input = "3^25+4";

        Shape shape = getShape(input);
        shape.getLeftChild().toggleCollapsed();

        resizeNodes(shape);

        shape.toggleCollapsed();
        resizeNodes(shape);

        shape.toggleCollapsed();
        resizeNodes(shape);

        Shape leftChild = shape.getLeftChild();
        int leftShapeRightEdge = leftChild.getEdgeXPosition() + leftChild.getWidth();
        int rightChildXPosition = shape.getRightChild().getEdgeXPosition();

        boolean ok = leftShapeRightEdge <= rightChildXPosition;
        assertEquals(true, ok);
    }

    @Test
    public void adjustNodeGoingOverEdgeOfScreen()
    {
        String input = "3^33+3^33+3^33*3^33";

        Shape shape = getShape(input);
        Shape rightMostPowerOperator = shape.getRightChild().getRightChild();
        rightMostPowerOperator.toggleCollapsed();

        int rightExtent = rightMostPowerOperator.getEdgeXPosition() +
                          rightMostPowerOperator.getWidth();

        //Log.e("rightExtent", "" + rightExtent);
        assertEquals(true, rightExtent < SCREEN_WIDTH);
    }
}
