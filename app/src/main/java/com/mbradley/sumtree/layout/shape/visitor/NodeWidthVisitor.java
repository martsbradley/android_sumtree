package com.mbradley.sumtree.layout.shape.visitor;

import android.graphics.Paint;

import com.mbradley.sumtree.layout.shape.Operation;
import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.view.treeFragment.TreeUtil;

public class NodeWidthVisitor implements Visitor
{
    private final Paint mPaint;
    
    public NodeWidthVisitor(Paint aPaint)
    {
        mPaint = aPaint;
    }

    @Override
    public void visit(Shape type)
    {
        operate(type);
        
        if (type.isParent())
        {
            visit(type.getLeftChild());

            visit(type.getRightChild());
        }
    }

    void operate(Shape type)
    {
        String drawnText = TreeUtil.getDrawnText(type);
        int stringSize = (int) mPaint.measureText(drawnText);
        ((Operation)type).setTextWidth(stringSize);
    }
}