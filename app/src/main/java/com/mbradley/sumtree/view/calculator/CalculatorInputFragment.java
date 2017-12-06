package com.mbradley.sumtree.view.calculator;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mbradley.sumtree.model.key.KeyValue;
import com.mbradley.sumtree.view.R;

import java.util.HashMap;
import java.util.Map;


public class CalculatorInputFragment extends LinearLayout implements View.OnClickListener
{
    private CalcPadInfo mCalcPadHandler;

    private final Map<Integer, KeyValue> keyMapper = new HashMap<>();

    public CalculatorInputFragment(Context aContext)
    {
        super(aContext);
        init(aContext);
    }

    public CalculatorInputFragment(Context aContext, AttributeSet attrs)
    {
        super(aContext, attrs);
        init(aContext);
    }

    public CalculatorInputFragment(Context aContext, AttributeSet attrs, int defStyle)
    {
        super(aContext, attrs, defStyle);
        init(aContext);
    }

    private void init(Context aContext) 
    {
        inflate(getContext(), R.layout.calc_fragment, this);
        setupKeys();

        if (aContext instanceof Activity)
        {
            mCalcPadHandler = (CalcPadInfo) aContext;
        }
    }

    public interface CalcPadInfo
    {
        void keyPressed(KeyValue aKey);
    }

    private void setupKeys()
    {
        setupKey(R.id.button0, KeyValue.ZERO);
        setupKey(R.id.button1, KeyValue.ONE);
        setupKey(R.id.button2, KeyValue.TWO);
        setupKey(R.id.button3, KeyValue.THREE);
        setupKey(R.id.button4, KeyValue.FOUR);
        setupKey(R.id.button5, KeyValue.FIVE);
        setupKey(R.id.button6, KeyValue.SIX);
        setupKey(R.id.button7, KeyValue.SEVEN);
        setupKey(R.id.button8, KeyValue.EIGHT);
        setupKey(R.id.button9, KeyValue.NINE);
        setupKey(R.id.buttonDecimalPlace, KeyValue.DOT);
        setupKey(R.id.buttonOpenBrace, KeyValue.OPEN_BRACE);
        setupKey(R.id.buttonCloseBrace, KeyValue.CLOSE_BRACE);
        setupKey(R.id.buttonAdd, KeyValue.ADD);
        setupKey(R.id.buttonSubtract, KeyValue.SUBTRACT);
        setupKey(R.id.buttonDivide, KeyValue.DIVIDE);
        setupKey(R.id.buttonMultiply, KeyValue.MULTIPLY);
        setupKey(R.id.buttonPower, KeyValue.POWER);
        setupKey(R.id.buttonBack, KeyValue.BACK);
        setupKey(R.id.buttonEquals, KeyValue.EQUALS);

//        TextView backbutton = (TextView) myView.findViewById( R.id.buttonBack);
//        backbutton.setCompoundDrawablesWithIntrinsicBounds(
//                             R.drawable.del8, 0, 0, 0);
        //  Image is also mentioned in the XML

    }

    private void setupKey(int aResourceId, KeyValue aKeyValue)
    {
        addClickListener(aResourceId);

        keyMapper.put(aResourceId, aKeyValue);
    }

    private void addClickListener(int buttonId)
    {
        View myButton =  findViewById(buttonId);
        myButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View button)
    {


        if (keyMapper.containsKey(button.getId()))
        {

            KeyValue value = keyMapper.get(button.getId());
            numberPressed(value, button);

        }
    }

    private void numberPressed(KeyValue aValue, final View aButton)
    {
        /* Rather than calling the sumtreeModel
         * Firstly grab the current expression.
         * from the Activity.
         *
         * if its equals check from the activity
         * if the expression is complete
         *
         *
         * A thread will handle the keypressing
         * and will call the activity with results
         * on the UI thread.
         * The result will be either
         * equals pressed or expressionUpdated
         */
//        CalculatorKeyPressThread thread = new CalculatorKeyPressThread(mCalcPadHandler,
//                                                                       mCalculatorDialogModel,
//                                                                       aValue);
//        thread.execute();
        mCalcPadHandler.keyPressed(aValue);

        CalculatorInputFeedbackThread inputThread = new CalculatorInputFeedbackThread(aButton);
        inputThread.execute();
    }
}
