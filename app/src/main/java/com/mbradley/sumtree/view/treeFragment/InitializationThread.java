package com.mbradley.sumtree.view.treeFragment;

import android.os.AsyncTask;
import android.util.Log;
import com.mbradley.sumtree.model.SumtreeParser;
import com.mbradley.sumtree.model.TreeLayout;
import com.mbradley.sumtree.model.key.ParseResult;
import com.mbradley.sumtree.sumdisplay.SumDisplay;

class InitializationThread extends AsyncTask<String, Integer, ParseResult>
{
    private final String mExpression;
    private final TreeFragment mTreeFragment;
    private final SumDisplay mSumDisplay;
    private final TreeLayout mTreeLayout;

    public InitializationThread(TreeFragment aTreeFragment,
                                SumDisplay aSumDisplay,
                                String aExpression)

    {
        mTreeFragment = aTreeFragment;
        mSumDisplay = aSumDisplay;
        mExpression = aExpression;

        mTreeLayout = new TreeLayout(mTreeFragment.getContext(),
                                     mTreeFragment.getTreeOrientation());
    }

    @Override
    protected ParseResult doInBackground(String[] aUnusedSomething)
    {

        SumtreeParser sumtreeParser = new SumtreeParser(mTreeLayout);

        ParseResult parseResult = sumtreeParser.parse(mExpression);

        if (parseResult == null)
        {
            parseResult = new ParseResult.Builder()
                                         .noUpdates()
                                         .build();
        }
        return parseResult;
    }

    @Override
    protected void onPostExecute(ParseResult aParseResult)
    {
        if (aParseResult.isUpdated())
        {
            if (aParseResult.getWidth() == -1)
            {
                Log.d("InitializationThread","2");
                mTreeFragment.setTree(aParseResult.getShape());
            }
            else
            {
                 Log.d("InitializationThread","3 width " +  aParseResult.getWidth() + " height " +
                         aParseResult.getHeight() );
                mTreeFragment.setTree(aParseResult.getShape(),
                                      aParseResult.getWidth(),
                                      aParseResult.getHeight());
            }
            mSumDisplay.setText(aParseResult.getExpression());
        }
        else if (aParseResult.wasZeroDivisionAttempt())
        {
            mTreeFragment.zeroDivisionAttempt();
        }
    }
}