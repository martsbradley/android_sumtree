package com.mbradley.sumtree.layout;

import com.mbradley.sumtree.layout.shape.Shape;

public class ShapeAndDistance
{
    private final Shape mNode;
    private final int mDistance;

    public ShapeAndDistance(Shape node,
                           int distance)
    {
        mNode = node;
        mDistance = distance;
    }

    public Shape getNode()
    {
        return mNode;
    }

    public int getDistance()
    {
        return mDistance;
    }

}
