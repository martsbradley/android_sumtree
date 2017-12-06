package com.mbradley.sumtree.layout.shape.visitor;


import com.mbradley.sumtree.layout.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class ShapesOnEachLevel implements Visitor
{
    private final Map<Integer, List<Shape>> mMap = new HashMap<>();
    private int mDepth = 0;

    @Override
    public void visit(Shape type)
    {
        mDepth++;
        operate(type);
        
        if (type.isParent())
        {
            visit(type.getLeftChild());

            visit(type.getRightChild());
        }
        mDepth--;
    }

    private void operate(Shape aShape) {
        List<Shape> list = mMap.get(mDepth);
        if (list == null) {
            list = new ArrayList<>();
            mMap.put(mDepth, list);
        }
        list.add(aShape);
    }

    public Map<Integer, List<Shape>> getLevelsMap()
    {
        return mMap;
    }
}
