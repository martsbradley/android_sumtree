package com.mbradley.sumtree.layout;

import com.mbradley.sumtree.layout.shape.Point;
import com.mbradley.sumtree.layout.shape.Shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Rank
{
    private static int NODE_SEPARATION = 190;  // Minimum distance between nodes on same row
    public static int RANK_HEIGHT     = 100;             // Height of a rank.
    public static int RANK_SEPARATION = 80;


    //aContext.getResources().getDisplayMetrics();


    private int mRankId;
    private int mRankBaseLine;
    private final List<Shape> mShapes = new ArrayList<>();

    public Rank(int aRankId)
    {
        mRankId = aRankId;
        setBaseLineFromRank(mRankId);
    }

    public static void setTextHeight(int textHeight)
    {
        NODE_SEPARATION = 6* textHeight;
        RANK_HEIGHT = 3* textHeight;
        RANK_SEPARATION = 3* textHeight;
    }

    public void appendNode(Shape shape)
    {
        shape.setEdgeYPosition(mRankBaseLine);
        int xPosition = 0;
        if (!mShapes.isEmpty()) {
            Shape rightMostNode = mShapes.get(mShapes.size() - 1);
            int position = rightMostNode.getPosition().x;
            int width = rightMostNode.getWidth();
            xPosition = position + width + RANK_SEPARATION;

        }
        shape.setXPosition(xPosition);
        mShapes.add(shape);
    }

    private void setBaseLineFromRank(int aRankIdx)
    {
        mRankBaseLine = ((aRankIdx) * RANK_HEIGHT) + (aRankIdx * RANK_SEPARATION) + 2;
    }

    public void moveNodesOverChildren() {
        int nodeCount = mShapes.size();

        for (int nodeIdx = 0; nodeIdx < nodeCount; ++nodeIdx) {
            Shape shape = mShapes.get(nodeIdx);

            if (shape.isParent()) {
                Point leftChild = shape.getLeftChildsPosition();
                Point rightChild = shape.getRightChildsPosition();
                Point thisNode = shape.getPosition();

                int newPosition = (leftChild.x + rightChild.x) / 2;
                newPosition = newPosition + (newPosition % 2);

                int currentPosition = thisNode.x;
                int distance = newPosition - currentPosition;

                moveNodeBy(shape, distance);
            }
        }
    }

    public void moveNodeBy(Shape aNode, int aDistance) {
        for (int NodeIdx = 0; NodeIdx < mShapes.size(); ++NodeIdx) {
            if (aNode == mShapes.get(NodeIdx)) {
                int IdxBack = mShapes.size();

                while (IdxBack > NodeIdx) {
                    --IdxBack;
                    Shape rNodeToMove = mShapes.get(IdxBack);
                    int OriginalPosition = rNodeToMove.getEdgeXPosition();
                    int NewPosition = OriginalPosition + aDistance;

                    boolean ValidMove = true;

                    if (IdxBack > 0 && aDistance < 0) {
                        //  There are nodes to the left of this one,
                        //  ensure that we are not trying to move the
                        //  shape into the area of the other shape.
                        Shape rNodeOnLeft = mShapes.get(IdxBack - 1);

                        int MinXPosition = rNodeOnLeft.getEdgeXPosition() +
                                rNodeOnLeft.getWidth() +
                                NODE_SEPARATION;

                        if (NewPosition < MinXPosition)
                            ValidMove = false;
                    }

                    if (ValidMove)
                        rNodeToMove.setXPosition(NewPosition);
                }

                break;
            }
        }
    }

    public ShapeAndDistance whichParentHasChildrenWhichNeedMoved() {
        int nodeCount = mShapes.size();
        ShapeAndDistance result = null;

        for (int nodeIdx = 0; nodeIdx < nodeCount; ++nodeIdx) {
            Shape shape = mShapes.get(nodeIdx);

            if (shape.isParent()) {
                Point LeftChild = shape.getLeftChildsPosition();
                Point RightChild = shape.getRightChildsPosition();
                Point ThisNode = shape.getPosition();

                int CurrentChildMidPoint = (LeftChild.x + RightChild.x) / 2;
                CurrentChildMidPoint = CurrentChildMidPoint + (CurrentChildMidPoint % 2);

                int ChildsMidPoint = ThisNode.x;

                if (Math.abs(ChildsMidPoint - CurrentChildMidPoint) > 5) {
                    int distance = ChildsMidPoint - CurrentChildMidPoint;
                    result = new ShapeAndDistance(shape, distance);
                    break;
                }
            }
        }
        return result;
    }

    public Integer getMaximumWidth() {
        Collection<Integer> widths = new ArrayList<>();
        for (Shape shape : mShapes) {
            int rightEdge = shape.getEdgeXPosition() + shape.getWidth();
            widths.add(rightEdge);
        }
        Integer maximum = Collections.max(widths);
        return maximum;
    }

//    public void centerTree(int diff) {
//        for (Shape shape : mShapes) {
//            int edge = shape.getEdgeXPosition();
//            shape.setXPosition(edge + diff);
//        }
//    }

    public void setRankId(int aRankId) {
        this.mRankId = aRankId;
        setBaseLineFromRank(mRankId);
        for (Shape shape : mShapes) {
            shape.setEdgeYPosition(mRankBaseLine);
        }
    }
}