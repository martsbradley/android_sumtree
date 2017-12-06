package com.mbradley.sumtree.view.treeFragment;

import android.os.AsyncTask;
import android.util.Log;

import com.mbradley.sumtree.layout.shape.DivideByZeroException;
import com.mbradley.sumtree.layout.shape.InfinityException;
import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.CompleteStatus;
import com.mbradley.sumtree.layout.shape.visitor.SumHighlightVisitor;
import com.mbradley.sumtree.model.SumtreeParser;
import com.mbradley.sumtree.model.TreeLayout;
import com.mbradley.sumtree.model.key.KeyPressOutcome;
import com.mbradley.sumtree.model.key.KeyValue;
import com.mbradley.sumtree.model.key.ParseResult;
import com.mbradley.sumtree.sumdisplay.SumDisplay;
import com.mbradley.sumtree.view.calculator.CalculatorDialogModel;

public class KeyHandlerThread extends AsyncTask<String, Integer, ParseResult>
{
    private final Shape mTree;
    private final TreeFragment mTreeFragment;
    private final CalculatorDialogModel mCalculatorDialogModel;
    private final KeyValue mKeyValue;
    private final SumDisplay mSumDisplay;
    private final float mTextSize;
    private final TreeLayout mTreeLayout;
    private final int mDrawViewWidth;

    public KeyHandlerThread(TreeFragment aTreeFragment,
                            SumDisplay aSumDisplay,
                            CalculatorDialogModel aCalculatorDialogModel,
                            KeyValue aKeyValue)

    {
        mTreeFragment = aTreeFragment;
        mSumDisplay = aSumDisplay;

        mTextSize = TreeUtil.getTextFontSizeFromStyle(aTreeFragment.getContext());

        mTree = aTreeFragment.getTree();
        mCalculatorDialogModel = aCalculatorDialogModel;
        mKeyValue = aKeyValue;

        mTreeLayout = new TreeLayout(mTreeFragment.getContext(),
                                     mTreeFragment.getTreeOrientation());

        mDrawViewWidth = mTreeFragment.getDrawViewWidth();


    }

    @Override
    protected ParseResult doInBackground(String[] aUnusedSomething)
    {
        String currentExpression = getCurrentExpression();
        mCalculatorDialogModel.setPreviousExpression(currentExpression);

        KeyPressOutcome keyPressResult = mCalculatorDialogModel.keyPressed(mKeyValue);

        ParseResult parseResult = null;

        if (keyPressResult.equalsWasPressed())
        {
            ResultString res = equalsWasPressed();

            if (res.wasToggled)
            {
                parseResult = new ParseResult.Builder()
                                             .shape(mTree)
                                             .expression(res.text)
                                             .build();
            }
            else if (res.completeStatus == CompleteStatus.MissingBrace)
            {
                parseResult = new ParseResult.Builder()
                                             .closingBraceMissing()
                                             .expression(currentExpression)
                                             .build();
            }
            else if (res.completeStatus == CompleteStatus.MissingNumber)
            {
                parseResult = new ParseResult.Builder()
                                                .finalNumberMissing()
                                                .expression(currentExpression)
                                                .build();
            }
            else if (res.mZeroDivison)
            {
                parseResult = new ParseResult.Builder()
                                             .zeroDivisionAttempted()
                                             .expression(currentExpression)
                                             .build();
            }
            else if (res.mInfinity)
            {
                parseResult = new ParseResult.Builder()
                                             .infinity()
                                             .expression(currentExpression)
                                             .build();
            }
        }
        else if (keyPressResult.expressionUpdated())
        {
            SumtreeParser sumtreeParser = new SumtreeParser(mTreeLayout);
            String expression = keyPressResult.getExpression();
            ////Log.d("Parsing","'" +expression+"'");
            parseResult = sumtreeParser.parse(expression);
        }
        else
        {
            //Log.d("error","why o why'");
        }

        if (parseResult == null)
        {
            //Log.d("Parsing","failed");
            parseResult = new ParseResult.Builder()
                                         .noUpdates()
                                         .build();
        }
        return parseResult;
    }

    private String getCurrentExpression()
    {
        SumHighlightVisitor highlight = new SumHighlightVisitor();
        mTree.accept(highlight);
        String currentExpression = highlight.getText();
        //Log.d("getCurrentExpression",currentExpression);
        return currentExpression;
    }



    private ResultString equalsWasPressed()
    {
        ResultString result = new ResultString();

        try
        {
            result.completeStatus = TreeUtil.completeStatus(mTree);
            if (result.completeStatus == CompleteStatus.IsComplete)
            {
                mTree.toggleCollapsed();

                DrawView.resizeNodes(mTree, mTextSize, mDrawViewWidth );

                SumHighlightVisitor highlight = new SumHighlightVisitor();
                mTree.accept(highlight);

                result.wasToggled = true;
                result.text = highlight.getText();
            }
        }
        catch (DivideByZeroException e)
        {
            mTree.toggleCollapsed();
            result.mZeroDivison = true;
            //Log.e("div","div problem ");
        }
        catch (InfinityException ex)
        {
            mTree.toggleCollapsed();
            result.mInfinity = true;
            //Log.e("inf", "inf problem ");
        }
        return result;
    }

    class ResultString
    {
        boolean wasToggled = false;
        boolean mZeroDivison = false;
        boolean mInfinity = false;
        CompleteStatus completeStatus = CompleteStatus.IsComplete;
        String text;
    }

    @Override
    protected void onPostExecute(ParseResult aParseResult)
    {
        if (aParseResult.isUpdated())
        {
            if (aParseResult.getWidth() == -1)
            {
                mTreeFragment.setTree(aParseResult.getShape());
            }
            else
            {
//                Log.d("KeyHandlerThread","3 width " +  aParseResult.getWidth() + " height " +
//                        aParseResult.getHeight() );
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
        else if (aParseResult.infinity())
        {
            mTreeFragment.infinity();
        }
        else if (aParseResult.unFinishedTree())
        {
            mTreeFragment.collapsedUnFinishedTree(aParseResult.getCompleteStatus());
        }
    }
}
