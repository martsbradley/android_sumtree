package com.mbradley.sumtree.view.calculator;


import com.mbradley.sumtree.model.key.KeyPressOutcome;
import com.mbradley.sumtree.model.key.KeyValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class needs to store the last valid expression
 * (mValidExpression) as well as the working expression.
 * (mPreviousExpression)
 * When the user presses equals if the equals
 * button then the 11111
 */
public class CalculatorDialogModel
{
    private final String mResetValue = "0";
    private String mPreviousExpression = mResetValue;
    private boolean isReset = true;
    private static final String OPERATORS = "+-*÷?";

    public CalculatorDialogModel()
    {
    }

    public void setPreviousExpression(String aPreviousExpression)
    {
      //  Log.e("PreviousExpression", "'" + aPreviousExpression + "'");
        mPreviousExpression = aPreviousExpression;
    }

    public KeyPressOutcome keyPressed(KeyValue aKeyValue)
    {
       // Log.e("keyPressed", "'" + aKeyValue.getText() + "'");

        if (aKeyValue.isEquals())
        {
            return KeyPressOutcome.equals(mPreviousExpression);
        }

        if (aKeyValue.isBackSpace())
        {
            if (isExpressionEmpty())
            {
                return getUpdatedValue(mPreviousExpression, aKeyValue);
            }
            if (mPreviousExpression.length() == 1)
            {
                isReset = true;
                return getUpdatedValue(mResetValue, aKeyValue);
            }
            else
            {
                int count = 1;
                String newValue = mPreviousExpression.substring(0, mPreviousExpression.length()-count);
                return getUpdatedValue(newValue, aKeyValue);
            }
        }

        if (cannotProcessKey(aKeyValue))
        {
            return getUpdatedValue(mPreviousExpression, aKeyValue);
        }

        String updatedExpression = getNewExpression(aKeyValue);
        return getUpdatedValue(updatedExpression, aKeyValue);
    }

    private KeyPressOutcome getUpdatedValue(String aMaybeUpdatedValue, KeyValue aKeyValue)
    {
        boolean isChanged = !aMaybeUpdatedValue.equals(mPreviousExpression);

        mPreviousExpression = aMaybeUpdatedValue;

        boolean newlyReset = (!isReset && mResetValue.equals(aMaybeUpdatedValue));
        //  Either the value has changed or the user pressed the zero key after being ine
        if (isChanged || newlyReset)
        {
            if (newlyReset && aKeyValue.isBackSpace())
            {
                isReset = true;
            }
            return KeyPressOutcome.updated(aMaybeUpdatedValue);
        }
        else
        {
            return KeyPressOutcome.noChange();
        }
    }

    private boolean cannotProcessKey(KeyValue aKeyValue)
    {
        boolean result = aKeyValue.isOperator() &&
                              (isReset || expressionEndsWithOperator());

        if (!result                    &&
            aKeyValue == KeyValue.ZERO &&
            mPreviousExpression.equals("0"))
        {
            result = true;
            isReset = false;
        }
        return result;
    }

    private boolean isExpressionEmpty()
    {
        return mResetValue.equals(mPreviousExpression);
    }

    private boolean expressionEndsWithOperator()
    {
        char lastCharacter = mPreviousExpression.charAt(mPreviousExpression.length() - 1);
        return isOperator(lastCharacter);
    }

    private boolean isOperator(char aText)
    {
        return OPERATORS.indexOf(aText) != -1;
    }
    private String getNewExpression(KeyValue aKeyValue)
    {
        if (aKeyValue.ismDecimalPlace())
        {
           if (!handleDecimal())
           {
               return mPreviousExpression;
           }
        }

        String newExpression;

        if (isReset ||
            ("0".equals(mPreviousExpression) &&
             !(aKeyValue.isOperator() || aKeyValue.ismDecimalPlace())))
        {
            newExpression = aKeyValue.getText();
        }
        else
        {
            newExpression = (mPreviousExpression + aKeyValue.getText());
        }
        isReset = false;


        return newExpression;
    }

    private boolean handleDecimal()
    {
        boolean handleDecimal = true;

        if (isExpressionEmpty())
        {
            isReset = false;
        }
        if (endsWithDecimal())
        {
            handleDecimal = false;
        }
        else if (expressionEndsWithOperator())
        {
            mPreviousExpression = mPreviousExpression + mResetValue;
        }
        return handleDecimal;
    }

    private boolean endsWithDecimal()
    {
        Pattern pattern = Pattern.compile(".*\\d\\.\\d+$");
        Matcher x = pattern.matcher(mPreviousExpression);
        return x.matches();
    }

    public void setReset(boolean aReset)
    {
        this.isReset = aReset;
    }
}