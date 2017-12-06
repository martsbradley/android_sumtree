package com.mbradley.sumtree.sumdisplay.android;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mbradley.sumtree.sumdisplay.HighlightStrategy;
import com.mbradley.sumtree.sumdisplay.Highlighted;
import com.mbradley.sumtree.sumdisplay.NotHighlighted;

public class SumDisplayImpl extends TextView implements com.mbradley.sumtree.sumdisplay.SumDisplay 
{
    private String mText = "";
    private HighlightStrategy mHighligher = new NotHighlighted("");

    public SumDisplayImpl(Context context) {
        super(context);
      //  Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
      //  this.setTypeface(face);
    }

    public SumDisplayImpl(Context context, AttributeSet attrs) 
    {
        super(context, attrs);

//        float textSize = updateTextSizeFromStyle(context);
//        setTextSize(textSize);
    }

    public SumDisplayImpl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
       // Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
       // this.setTypeface(face);
    }



    @Override
    public void setText(String aText) {
        //checkWidget();
        this.mText = aText;
        super.setText(mText, BufferType.SPANNABLE);
       // mHighligher = new NotHighlighted(mText);

        // use redraw() instead of directly calling
        // the paintBorderAndText(GC gc) method.
       // redraw();
    }


    @Override
    public void setExtends(int aStart, int aEnd)
    {
        mHighligher = new Highlighted(mText, aStart, aEnd);

        setText(mText, BufferType.SPANNABLE);
        Spannable s = (Spannable)super.getText();
        mHighligher.getBeginning();
        int start = mHighligher.getBeginning().length();
        int end = start + mHighligher.getMiddle().length();
        s.setSpan(new ForegroundColorSpan(0xFFFF0000), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        redraw();
    }

    @Override
    public void clearExtents()
    {
        mHighligher = new NotHighlighted(mText);

        SpannableString ss=(SpannableString)super.getText();

        ForegroundColorSpan[] spans = ss.getSpans(0, super.getText().length(), ForegroundColorSpan.class);
        for(ForegroundColorSpan span : spans)
        {
            ss.removeSpan(span);
        }
        redraw();
    }

    private void redraw() {
        invalidate();
    }

    public String getText() {
        return mText;
    }
}
