package com.mbradley.sumtree.view.treeFragment;

import android.content.Context;
import android.widget.TextView;

import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.layout.shape.visitor.CompleteStatus;
import com.mbradley.sumtree.layout.shape.visitor.SumIsCompleteVisitor;
import com.mbradley.sumtree.view.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TreeUtil
{
    public static CompleteStatus completeStatus(Shape aTree)
    {
        SumIsCompleteVisitor visitor = new SumIsCompleteVisitor();
        aTree.accept(visitor);
        return visitor.sumIsComplete();
    }

    public static boolean isCalculation(Shape op)
    {
        return op.getLeftChild()  != null  &&
               op.getRightChild() != null;
    }

    public static boolean isCollapsedCalculation(Shape op)
    {
        boolean result = op.isCollapsed()           &&
                isCalculation(op);
        return result;
    }


    // if its too large a number
    // if there are too many decimal places
    // don't draw them all.






    private static String getText(Shape aOperation)
    {
        String result = aOperation.getText();
        boolean isCalc = isCalculation(aOperation) ;
        boolean isCollapsed = isCollapsedCalculation(aOperation);
      //  Log.w("TreeUtil",  result + " calc =" + isCalc + " is collapsed " + isCollapsed + " result " +result + "science?" + aOperation.getShowsAsScientific());

        if ((!isCalc || isCollapsed) &&
            (aOperation.getShowsAsScientific() /*|| result.length() > 10*/))
        {
            BigDecimal decimal = new BigDecimal(result);
            NumberFormat formatter = new DecimalFormat("0.######E0");
            result = formatter.format(decimal.doubleValue());
        }
        //Log.w("TreeUtil",  "result is " + result);

        return result;
    }
    public static String getDrawnText(Shape op)
    {
        String text = getText(op);

        if (isCollapsedCalculation(op))
        {
            if (anyParentHasOpenBrace(op))
            {
                text =  "(" + text;
            }
            if (anyParentHasCloseBrace(op))
            {
                text =  text + ")";
            }
        }
        else if (!TreeUtil.isCalculation(op))
        {
            if (anyParentHasOpenBrace(op))
            {
                text =  "(" + text;
            }

            if (anyParentHasCloseBrace(op))
            {
                text =  text + ")";
            }
        }
        return text;
    }



    private static boolean anyParentHasOpenBrace(Shape op)
    {
        boolean openBraceFound = op.hasOpenBrace();

        Shape parent = op.getParent();

        while (parent != null && !openBraceFound)
        {
            boolean isLeftChild = parent.getLeftChild() == op;

            if (parent.hasOpenBrace() &&
                    isLeftChild)
            {
                openBraceFound = true;
            }

            if (!isLeftChild)
            {
                break;
            }
            op = parent;
            parent = op.getParent();
        }
        return openBraceFound;
    }

    private static boolean anyParentHasCloseBrace(Shape op)
    {
        boolean openBraceFound = op.hasCloseBrace();

        Shape parent = op.getParent();

        while (parent != null && !openBraceFound)
        {
            boolean isRightChild = parent.getRightChild() == op;

            if (parent.hasCloseBrace() &&
                    isRightChild)
            {
                openBraceFound = true;
            }

            if (!isRightChild)
            {
                break;
            }
            op = parent;
            parent = op.getParent();
        }
        return openBraceFound;
    }

    public static int getTextFontSizeFromStyle(Context context)
    {
        if (mTextSize == -1)
        {
            TextView t = new TextView(context);
            t.setTextAppearance(context, R.style.CalcInputStyle);
            mTextSize = (int)t.getTextSize();
        }
        return mTextSize;
    }
    private static int mTextSize = -1;
}
