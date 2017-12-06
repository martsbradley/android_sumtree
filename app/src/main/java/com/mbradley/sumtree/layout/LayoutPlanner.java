package com.mbradley.sumtree.layout;

import com.mbradley.sumtree.layout.shape.Shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LayoutPlanner 
{
    private final List<Rank> mRanks = new ArrayList<>();

    private final EOrientation mOrientation;
    
    public LayoutPlanner(EOrientation aOrientation)
    {
        mOrientation = aOrientation;
    }

    public void addNode(Shape node, int aRank)
    {
        int rankCount = mRanks.size();
        //assert(aRank <= rankCount);

        if(aRank == rankCount)
        {
            mRanks.add(aRank, new Rank(aRank));
        }

        //assert(aRank < mRanks.size());

        Rank rank = mRanks.get(aRank);
        rank.appendNode(node);
    }
    
    public void layout()
    {
        // Start at the rank second from the bottom
        // Start at the last node in the rank.
        // Ask what are the Center of the left and right children
        // Move the node to the center of its children
        //
        // After this is done for each rank go back and move any children
        // that are too far from their parents under them
        //

        if (mOrientation == EOrientation.ETreeUp)
        {
            int MaxRank = mRanks.size()- 1;

            for (int Idx = 0; Idx < mRanks.size(); ++Idx)
            {
                mRanks.get(Idx).setRankId(MaxRank - Idx);
            }
        }

        boolean result = true;
        int Times = 0;

        while (result && Times++ < 10000)
        {
            moveParents();
            result = moveChildren();
        }

       // Log.d("LayoutPlanner","Times =" + Times);
    }

//    public void centerTree(int diff)
//    {
//        /*  Need to find the maximum width so that all the shapes
//         *  can be shifted to center the image within the client
//         *  area.
//         */
//        for (Rank rank : mRanks)
//        {
//            rank.centerTree(diff);
//        }
//    }
    public int maximumHeight()
    {
        int ranks = mRanks.size();

        return ranks* Rank.RANK_HEIGHT + (ranks-1)* Rank.RANK_SEPARATION;
    }
    public int maximumWidth()
    {
        Collection<Integer> widths = new ArrayList<>();

        widths.add(0);// Avoid an empty collection

        for (Rank rank : mRanks)
        {
            Integer width = rank.getMaximumWidth();
            widths.add(width);
        }
        Integer rightMostPoint = Collections.max(widths);
        
        return rightMostPoint;
    }


    private void moveParents()
    {
        int rankCount = mRanks.size();
        int rankId = rankCount-1;

        while (rankId > 0)
        {
            --rankId;
            mRanks.get(rankId).moveNodesOverChildren();
        }
    }
    private boolean moveChildren()
    {
        boolean ChildMoved = false;
        //  Now move any children right are too far left
        //  from their parents.
        for (int Idx = 0; Idx < mRanks.size(); ++Idx)
        {
            Rank rank = mRanks.get(Idx);

            ShapeAndDistance info = rank.whichParentHasChildrenWhichNeedMoved();

                    
            if (info != null)
            {
                Shape parent = info.getNode();
                int distance = info.getDistance();
                mRanks.get(Idx+1).moveNodeBy(parent.getLeftChild(), distance);
                ChildMoved = true;
                //break;
            }
        }
        return ChildMoved;
    }

    public void clearRanks()
    {
        mRanks.clear();
    }
}
