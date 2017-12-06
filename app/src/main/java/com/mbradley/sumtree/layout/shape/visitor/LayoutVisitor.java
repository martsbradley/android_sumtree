package com.mbradley.sumtree.layout.shape.visitor;

import com.mbradley.sumtree.layout.LayoutPlanner;
import com.mbradley.sumtree.layout.shape.Shape;


public class LayoutVisitor implements Visitor
{
    private final LayoutPlanner mLayout;
    private int mRank = 0;
    
    public LayoutVisitor(LayoutPlanner arLayout)
    {
        mLayout = arLayout;
        mLayout.clearRanks();
    }

    @Override
    public void visit(Shape type)
    {
        if (type == null)
        {
            return;
        }

        operate(type);
        
        if (type.isParent())
        {
            visit(type.getLeftChild());

            visit(type.getRightChild());
        }
        processVisitDone();
    }

    private void operate(Shape type)
    {
        processVisit(type);
    }

    private void processVisit(Shape aShape)
    {
        mLayout.addNode(aShape, mRank);
        ++mRank;
    }

    private void processVisitDone()
    {
        --mRank;
    }
}