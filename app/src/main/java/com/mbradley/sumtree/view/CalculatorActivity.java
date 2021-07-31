package com.mbradley.sumtree.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mbradley.sumtree.model.key.KeyValue;
import com.mbradley.sumtree.sumdisplay.SumDisplay;
import com.mbradley.sumtree.sumdisplay.android.SumDisplayImpl;
import com.mbradley.sumtree.view.calculator.CalculatorDialogModel;
import com.mbradley.sumtree.view.calculator.CalculatorInputFragment;
import com.mbradley.sumtree.view.treeFragment.ClearButtonView;
import com.mbradley.sumtree.view.treeFragment.FlipButtonView;
import com.mbradley.sumtree.view.treeFragment.KeyHandlerThread;
import com.mbradley.sumtree.view.treeFragment.TreeFragment;


public class CalculatorActivity extends AppCompatActivity
                implements CalculatorInputFragment.CalcPadInfo,
        SumDisplay
{
    private static final String ExpressionSharedPref = "THE_EXPRESSION";
    private TreeFragment mTreeView;
    private SumDisplayImpl mSumDisplay;
    private final CalculatorDialogModel mCalculatorDialogModel = new CalculatorDialogModel();



    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.calculator_activity);

        String expr = mTreeView.getExpression();

        mTreeView = (TreeFragment)findViewById(R.id.treefragment);

        mSumDisplay = (SumDisplayImpl) findViewById(R.id.sumDisplay);
        ClearButtonView mClearButtonView = (ClearButtonView) findViewById(R.id.clear);
        FlipButtonView mFlipButtonView = (FlipButtonView) findViewById(R.id.flip);
        mClearButtonView.setTreeButtonHandler(mTreeView);
        mFlipButtonView.setTreeButtonHandler(mTreeView);


        mTreeView.expressionUpdated(expr);
    }
    @Override
    protected void onCreate(Bundle aSavedInstanceState)
    {
        super.onCreate(aSavedInstanceState);
        //Log.e("onCreate", "zzzzzzzz");
        setContentView(R.layout.calculator_activity);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        mTreeView = (TreeFragment)findViewById(R.id.treefragment);

        mSumDisplay = (SumDisplayImpl) findViewById(R.id.sumDisplay);

        ClearButtonView mClearButtonView = (ClearButtonView) findViewById(R.id.clear);
        FlipButtonView mFlipButtonView = (FlipButtonView) findViewById(R.id.flip);
        mClearButtonView.setTreeButtonHandler(mTreeView);
        mFlipButtonView.setTreeButtonHandler(mTreeView);

    }
    @Override
    public void onPause()
    {
       // getAdsView().pause();
        super.onPause();
        //Log.e("onPau", "xxxxxxxxxxxxx");

        String expression = mTreeView.getExpression();
        savePreferenceValue(expression);
    }

    private void savePreferenceValue(String expression)
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(ExpressionSharedPref, expression);
        editor.commit();
    }

    @Override
    public void onResume()
    {


        super.onResume();
        //Log.e("onReme", "yyyyyyyyy");

        String loadedExpr = getStoredExpr();

        if (!"0".equalsIgnoreCase(loadedExpr))
        {
            mCalculatorDialogModel.setReset(false);
            mCalculatorDialogModel.setPreviousExpression(loadedExpr);
        }
        mTreeView.expressionUpdated(loadedExpr);

        //getAdsView().resume();
    }
    @Override
    public void onDestroy()
    {
        savePreferenceValue("0");
        //Log.e("onDes", "aaaaaaaaaaa");


        //getAdsView().destroy();
        super.onDestroy();

    }
    private String getStoredExpr()
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String val = sharedPref.getString(ExpressionSharedPref, "0");
        return val;
    }

    @Override
    public void keyPressed(KeyValue aKey)
    {
        KeyHandlerThread thread = new KeyHandlerThread(mTreeView,
                                                       this,
                                                       mCalculatorDialogModel,
                                                       aKey);
        thread.execute();

    }

    @Override
    public void setText(String aText) 
    {
        mSumDisplay.setText(aText);
    }

    @Override
    public void setExtends(int aStart, int aEnd) 
    {
        mSumDisplay.setExtends(aStart, aEnd);
    }

    @Override
    public void clearExtents() 
    {
        mSumDisplay.clearExtents();
    }
}
