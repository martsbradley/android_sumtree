package com.mbradley.sumtree.layout.shape.visitor; 
import com.mbradley.sumtree.layout.shape.Shape;

public class SumHighlightVisitor implements Visitor{

	private static final int INVALID = -1;
	private int mHighlightStart = INVALID;
	private int mHighlightEnd = INVALID;
	private final StringBuilder mSb = new StringBuilder();


	@Override
	public void visit(Shape aOp) 
	{
		checkHighlightStart(aOp);
		if (aOp.hasOpenBrace()) mSb.append("(");

		if (aOp.isParent())
		{
			visit(aOp.getLeftChild());
			operate(aOp);
			visit(aOp.getRightChild());
		}
		else
		{
		    operate(aOp);
		}
		if (aOp.hasCloseBrace()) mSb.append(")");
		checkHighlightEnd(aOp);
	}


	private void operate(Shape op) 
	{
		String text = op.getText();	
		mSb.append(text);
	}

	private void checkHighlightStart(Shape aOp)
	{
		if (aOp.containsMouse())
		{
			//assert(mHighlightStart == INVALID);
			//assert(mHighlightEnd == INVALID);
			mHighlightStart = mSb.length();
		}
	}

	private void checkHighlightEnd(Shape aOp) 
	{
		if (aOp.containsMouse())
		{
			//assert(mHighlightStart != INVALID);
			//assert(mHighlightEnd == INVALID);
			mHighlightEnd = mSb.length();
		}
	}

	public String getText() 
	{
		return mSb.toString();
	}


	public int getHightlightStart() {
		return mHighlightStart;
	}
	public int getHightlightEnd() 
	{
		return mHighlightEnd;
	}

	public boolean hasHightlightText() {
		 return mHighlightStart != INVALID &&
				mHighlightEnd != INVALID;
	}
}
