package com.mbradley.sumtree.view.calculator;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;


/**
 * Waits a little while then resets the
 */
class CalculatorInputFeedbackThread extends AsyncTask<String, Integer, String>
{
    private TextView mTextView;
    private boolean mResetToNormal = false;


    public CalculatorInputFeedbackThread(View aTextView)
    {
        if (aTextView  instanceof TextView)
        {
            this.mTextView = (TextView) aTextView;
            mTextView.setTypeface(null, Typeface.BOLD);
//            mTextView.setTextSize(mTextView.getTextSize()+1);
            mResetToNormal = true;
        }

    }

    @Override
    protected String doInBackground(String[] expression)
    {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e)
        {
            //  OK
        }

        return null;
    }

    @Override
    protected void onPostExecute(String aKeyPressOutcome)
    {

        if (mResetToNormal)
        {
            mTextView.setTypeface(null, Typeface.NORMAL);

//            mTextView.setTextSize(mTextView.getTextSize()-1);
        }
    }
}
