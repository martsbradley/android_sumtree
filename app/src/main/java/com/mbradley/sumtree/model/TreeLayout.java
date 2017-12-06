package com.mbradley.sumtree.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mbradley.sumtree.layout.EOrientation;
import com.mbradley.sumtree.layout.LayoutPlanner;
import com.mbradley.sumtree.layout.Rank;
import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.LayoutVisitor;
import com.mbradley.sumtree.layout.shape.visitor.NodeWidthVisitor;
import com.mbradley.sumtree.layout.shape.visitor.SumHighlightVisitor;
import com.mbradley.sumtree.model.key.ParseResult;
import com.mbradley.sumtree.view.treeFragment.TreeUtil;


public class TreeLayout implements ShapeLayoutPlanner
{
    private final Paint mBlackPaint = new Paint();
    private final EOrientation mTreeOrientation;

    public TreeLayout(Context aContext, EOrientation aTreeOrientation)
    {
//        Typeface bold = Typeface.create(Typeface.SERIF, Typeface.BOLD);
//        mBlackPaint.setTypeface(bold);

        int textSize = TreeUtil.getTextFontSizeFromStyle(aContext);
        mBlackPaint.setTextSize(textSize);
        mBlackPaint.setColor(Color.BLACK);


        Rect bounds = new Rect();
        mBlackPaint.getTextBounds("1", 0, 1, bounds);


        //Log.e("bounds", bounds.height() +" ");
        Rank.setTextHeight(bounds.height());
        mTreeOrientation = aTreeOrientation;
    }

    @Override
    public ParseResult getParseResult(Shape shape)
    {
        //   This visitor updates each Shape in the tree
        //   with the appropriate width
        NodeWidthVisitor width = new NodeWidthVisitor(mBlackPaint);
        shape.accept(width);

        final LayoutPlanner planner = new LayoutPlanner(mTreeOrientation);
        LayoutVisitor layout = new LayoutVisitor(planner);
        shape.accept(layout);

        planner.layout();
        int maxWidth = planner.maximumWidth();
        int maxHeight = planner.maximumHeight();

        SumHighlightVisitor highlight = new SumHighlightVisitor();
        shape.accept(highlight);
        String expression = highlight.getText();

        return new ParseResult.Builder()
                                .shape(shape)
                                .width(maxWidth)
                                .height(maxHeight)
                                .expression(expression)
                                .build();
    }
    //  Need to be able to layout the tree as if it is not collapsed
    //  but then displayed the sum as if the tree is collapsed
    //  That will allow the result to be drawn in the correct
    //  position
}
