package com.mbradley.sumtree.model;

import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.PopulateBlankShapeVisitor;
import com.mbradley.sumtree.model.key.ParseResult;
import com.mbradley.sumtree.parser.IncompleteSumParser;


public class SumtreeParser
{
    private final ShapeLayoutPlanner mLayoutPlanner;

    public SumtreeParser(ShapeLayoutPlanner aLayoutPlanner)
    {
        mLayoutPlanner = aLayoutPlanner;
    }

    public ParseResult parse(String aExpression)
    {
        ParseResult result;

        try {
            result = myParse(aExpression);
        }
        catch (RuntimeException e)
        {
            result = new ParseResult.Builder()
                                    .noUpdates()
                                    .build();
        }
        return result;
    }

    private ParseResult myParse(String aExpression) throws RuntimeException
    {

        IncompleteSumParser incompleteSumParser = new IncompleteSumParser(aExpression);
        final Shape shape = incompleteSumParser.parse();

        PopulateBlankShapeVisitor populator = new PopulateBlankShapeVisitor();
        shape.accept(populator);

        ParseResult result = mLayoutPlanner.getParseResult(shape);

        return result;
    }

}
