package com.mbradley.sumtree.model.key;


public class KeyPressOutcome
{
    private final boolean mExpressionUpdated;
    private final String mExpression;
    private final boolean mEqualsPressed;

    private KeyPressOutcome(boolean mExpressionUpdated,
                            String mExpression,
                            boolean aEqualsPressed)
    {
        this.mExpressionUpdated = mExpressionUpdated;
        this.mExpression = mExpression;
        this.mEqualsPressed = aEqualsPressed;
    }

    public static KeyPressOutcome noChange()
    {
        return new KeyPressOutcome(false, "NOCHANGE!", false);
    }
    public static KeyPressOutcome updated(String aUpdatedExpression)
    {
        return new KeyPressOutcome(true, aUpdatedExpression, false);
    }

    public static KeyPressOutcome equals(String aUpdatedExpression)
    {
        return new KeyPressOutcome(false, aUpdatedExpression, true);
    }


    /** @return the latest expression **/
    public String getExpression()
    {
        if (!mExpressionUpdated && !mEqualsPressed)
        {
            throw new IllegalStateException();
        }
        return mExpression;
    }

    /** @return true if the expression needs parsed again*/
    public boolean expressionUpdated()
    {
        return mExpressionUpdated;
    }


    public boolean equalsWasPressed()
    {
        return mEqualsPressed;
    }
}
