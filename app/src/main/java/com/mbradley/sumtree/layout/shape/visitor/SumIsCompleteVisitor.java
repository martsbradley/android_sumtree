package com.mbradley.sumtree.layout.shape.visitor;


import com.mbradley.sumtree.layout.shape.BlankOperation;
import com.mbradley.sumtree.layout.shape.BraceOperation;
import com.mbradley.sumtree.layout.shape.Shape;

public class SumIsCompleteVisitor implements Visitor
{
    private CompleteStatus mStatus = CompleteStatus.IsComplete;

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
        if (aShape instanceof BlankOperation)
        {
            mStatus = CompleteStatus.MissingNumber;
        }

        if (aShape instanceof BraceOperation ||
            (aShape.hasOpenBrace() && !aShape.hasCloseBrace()))
        {
            mStatus = CompleteStatus.MissingBrace;
        }
    }

    public CompleteStatus sumIsComplete()
    {
        return mStatus;
    }
}
