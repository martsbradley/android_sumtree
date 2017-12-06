package com.mbradley.sumtree.layout.shape.visitor;

import android.util.Log;

import com.mbradley.sumtree.layout.shape.Shape;

import java.util.List;
import java.util.Map;


/**
 *  This class is really needed for when the user adjusts
 *  the tree by clicking a operator, a sub tree collapses
 *  Program needs to ensure that the result is shown properly
 *  and there is the right amount of space provided.
 */
public class NodeOverlapPrevention
{
    private final Map<Integer, List<Shape>> mMap;
    private final int mScreenWidth;
    private final NodeWidthVisitor mWidthVisitor;

    public NodeOverlapPrevention(Map<Integer, List<Shape>> aMap,
                                 NodeWidthVisitor aWidthVisitor,
                                 int aScreenWidth)
    {
        mMap = aMap;
        mWidthVisitor = aWidthVisitor;
        mScreenWidth = aScreenWidth;
    }


    public void adjustShapesOnEachLevel()
    {
        for (Map.Entry<Integer, List<Shape>> entry: mMap.entrySet())
        {
            List<Shape> shapesOnLevel = entry.getValue();

            adjustLeftOperand(shapesOnLevel);


            stopExtendingOverScreenEdge(shapesOnLevel);
        }
    }

    private void stopExtendingOverScreenEdge(List<Shape> aShapesOnLevel)
    {
        int priorShapeRightEdge = 0;

        Shape priorShape = null;
        for (int idx = 0; idx < aShapesOnLevel.size(); idx++)
        {
            Shape shape = aShapesOnLevel.get(idx);

            if (priorShape != null)
            {
                priorShapeRightEdge = getRightEdge(priorShape);
            }

            int nextShapeRightEdge = mScreenWidth;
            int indexOfNextShape = idx+1;

            if (indexOfNextShape < aShapesOnLevel.size())
            {
                Shape nextShape = aShapesOnLevel.get(indexOfNextShape);
                nextShapeRightEdge = nextShape.getEdgeXPosition();
            }

            if (extendsPastEdgeOfScreen(shape, nextShapeRightEdge))
            {
                stopExtendingOverScreenEdge(shape, priorShapeRightEdge, nextShapeRightEdge);
            }
            priorShape = shape;
        }
    }
    private void stopExtendingOverScreenEdge(Shape aShape, int aMinX, int aMaxX)
    {
        //Log.e("edge", aMinX + " " + aShape.getEdgeXPosition());

        boolean tooLong = aMinX + aShape.getWidth() > aMaxX;

        if (tooLong)
        {
            aShape.setShowsAsScientific(true);
        }
        mWidthVisitor.operate(aShape);

        // position midway between min max

        aShape.setXPosition(aMinX + (aMaxX - aMinX - aShape.getWidth())/2);
    }

    private boolean extendsPastEdgeOfScreen(Shape aShape, int aMaxX)
    {
        int rightExtent = getRightEdge(aShape);
        return rightExtent > aMaxX;
    }

    private int getRightEdge(Shape aShape)
    {
        return aShape.getEdgeXPosition() + aShape.getWidth();
    }

    private void adjustLeftOperand(List<Shape> aShapesOnLevel)
    {
        // Start at the back
        // grab the back one, its the right.
        // then grab the one before it.
        // if they have the same parent then process them.

        int idx = aShapesOnLevel.size() -1;
        while (idx > 0)
        {
            Shape potentialRHS = aShapesOnLevel.get(idx);
            Shape potentialLHS = aShapesOnLevel.get(idx-1);
            boolean siblings = potentialLHS.getParent() == potentialRHS.getParent();
            if (siblings)
            {
                int lhsMinXPos = 0;
                if (idx-1 > 0)
                {
                    Shape shapeBeforeLHS = aShapesOnLevel.get(idx-2);
                    lhsMinXPos = shapeBeforeLHS.getEdgeXPosition() +
                                 shapeBeforeLHS.getWidth();

                }

                if (overlaps(potentialLHS, potentialRHS))
                {
                    if (enoughSpace(potentialLHS, potentialRHS, lhsMinXPos))
                    {
                        doAdjustLeftOperand(potentialLHS, potentialRHS, lhsMinXPos);
                    }
                    else
                    {
                        // need to make this big number display smaller.
                        potentialLHS.setShowsAsScientific(true);
                        mWidthVisitor.operate(potentialLHS);
                        doAdjustLeftOperand(potentialLHS, potentialRHS, lhsMinXPos);
                    }
                }
                idx = idx - 2;
            }
            else {
                idx--;
            }
        }
    }

    private boolean enoughSpace(Shape leftNumber, Shape rightNumber, int aLhsMinXPos)
    {
        int overlapsBy = overLapsBy(leftNumber, rightNumber);
        int xPosition = leftNumber.getEdgeXPosition() - overlapsBy;
        boolean enough = xPosition >= aLhsMinXPos;
        return enough;
    }

    private void doAdjustLeftOperand(Shape leftNumber, Shape rightNumber, int aLhsMinXPos)
    {
        int overlapsBy = overLapsBy(leftNumber, rightNumber);
        int xPosition = leftNumber.getEdgeXPosition() - overlapsBy;

        xPosition = (aLhsMinXPos+ xPosition)/2;

        leftNumber.setXPosition(xPosition);
    }

    private int overLapsBy(Shape leftNumber, Shape rightNumber)
    {
        int leftExtent = rightNumber.getEdgeXPosition();
        int rightExtent = leftNumber.getEdgeXPosition() + leftNumber.getWidth();
        int overlapsBy = rightExtent - leftExtent;
        overlapsBy++;
        return overlapsBy;
    }

    public static boolean overlaps(Shape aLeft, Shape aRight)
    {
        int rightExtent = aLeft.getEdgeXPosition() + aLeft.getWidth();
        int leftExtent = aRight.getEdgeXPosition();
        return rightExtent > leftExtent;
    }
}
