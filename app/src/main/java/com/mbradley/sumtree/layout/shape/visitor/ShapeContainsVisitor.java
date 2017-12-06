package com.mbradley.sumtree.layout.shape.visitor;

//import org.eclipse.swt.graphics.Point;

import com.mbradley.sumtree.layout.shape.Point;
import com.mbradley.sumtree.layout.shape.Shape;

public class ShapeContainsVisitor implements Visitor
{
    private final Point mPoint;
    private Shape mResult = null;

    public ShapeContainsVisitor(Point aPoint)
    {
        mPoint = aPoint;
    }

    @Override
    public void visit(Shape aShape)
    {
        operate(aShape);
        
        if (aShape.isParent())
        {
            visit(aShape.getLeftChild());

            visit(aShape.getRightChild());
        }
    }

    private void operate(Shape aShape)
    {
        if (aShape.contains(mPoint))
        {
            mResult = aShape;
            mResult.setMouseOver(true);
        }
        else
            aShape.setMouseOver(false);
    }
    
    public boolean isContained()
    {
        return mResult != null;
    }
    public Shape getResult()
    {
        return mResult;
    }
}
