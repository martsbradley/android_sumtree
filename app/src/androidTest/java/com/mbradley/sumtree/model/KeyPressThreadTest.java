package com.mbradley.sumtree.model;

import android.test.InstrumentationTestCase;

import com.mbradley.sumtree.model.key.KeyPressOutcome;
import com.mbradley.sumtree.model.key.ParseResult;
import com.mbradley.sumtree.model.key.KeyValue;
import com.mbradley.sumtree.view.calculator.CalculatorDialogModel;

import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
public class KeyPressThreadTest extends InstrumentationTestCase
{
    private boolean called;

    String mExpression= "1+2";
    KeyValue mKeyThatWasPressed = KeyValue.ADD;
    KeyPressOutcome mOutcome = KeyPressOutcome.updated("1+2+");
    boolean mParseSuccess = true;


    public void testMyStuff()
    {
        try
        {
            doit();
        }
        catch (Throwable e)
        {
            System.out.println("failed it");
        }
    }
    public void doit() throws Throwable
    {
        // create  a signal to let us know when our task is done.
        final CountDownLatch signal = new CountDownLatch(1);


        //final SumtreeModel treeModel = Mockito.mock(SumtreeModel.class);
        final SumtreeParser treeParser = Mockito.mock(SumtreeParser.class);
        final ParseResult threadResult = Mockito.mock(ParseResult.class);
        CalculatorDialogModel mKeyPadModel = Mockito.mock(CalculatorDialogModel.class);
        //ExpressionCalculator expressionCal = Mockito.mock(ExpressionCalculator.class);

        when(mKeyPadModel.keyPressed(mKeyThatWasPressed)).thenReturn(mOutcome);
       // when(expressionCal.getExpression()).thenReturn(mExpression);

        if (mParseSuccess)
        {
            when(threadResult.isUpdated()).thenReturn(false);
            when(treeParser.parse(anyString())).thenReturn(threadResult);
        }
        else
        {
            when(treeParser.parse(anyString())).thenThrow(RuntimeException.class);
            when(threadResult.isUpdated()).thenReturn(true);
        }


       /**
        * THIS STUFF WORKS, ITS GOOD EXAMPLE.
        *
        *
        * IKeyProcessor IKeyProcessor2 = new KeyPressProcessor(expressionCal,mKeyThatWasPressed, mKeyPadModel);

        final KeyPressThread keyProcessor = new KeyPressThread(treeModel, IKeyProcessor2,
                                                               treeParser)
        {
            @Override
            protected void onPostExecute(ParseResult result)
            {
                super.onPostExecute(result);

//                 * This is the key, normally you would use some type of listener
//                 * to notify your activity that the async call was finished.
//                 *
//                 * In your test method you would subscribe to that and signal
//                 * from there instead.

                called = true;
                signal.countDown();
            }
        };


        // Execute the async task on the UI thread! THIS IS KEY!
        runTestOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                keyProcessor.execute();
            }
        });


        */

//	     * The testing thread will wait here until the UI thread releases it
//	     * above with the countDown() or 30 seconds passes and it times out.
//	     *
        signal.await(2, TimeUnit.SECONDS);
        // The task is done, and now you can assert some things!
        assertTrue("Happiness", called);
    }
}