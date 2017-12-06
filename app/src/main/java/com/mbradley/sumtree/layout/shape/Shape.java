package com.mbradley.sumtree.layout.shape;

import com.mbradley.sumtree.layout.shape.visitor.Visitor;

import java.math.BigDecimal;

public interface Shape
{
    void setShowsAsScientific(boolean aShowsAsScientific);
    boolean getShowsAsScientific();

    boolean contains(Point aPoint);
    void setEdgeYPosition(int aEdgeYPosition);
    Shape getParent();

    Point getPosition();
    int getEdgeXPosition();
    void setXPosition(int xPosition);
    boolean isParent();

    Point getLeftChildsPosition();
    Point getRightChildsPosition();

    int getWidth();
    //void draw(GC graphicsCtx, Display display);

    void setChild(Shape aShape);
    void setParent(Shape pParent);
    Shape getLeftChild();
    Shape getRightChild();
    int getEdgeYPosition();
   
    void accept(Visitor visitor);
    void setMouseOver(boolean isMouseOver);
    String getText();
    BigDecimal getValue();

    BigDecimal operate(BigDecimal left, BigDecimal right);

    boolean containsMouse();
	Point getBottomCenter();
	void toggleCollapsed();
	Point getTopCenter();
	boolean isCollapsed();

    boolean hasOpenBrace();
    boolean hasCloseBrace();
    void setOpenBrace(boolean aValue);
    void setCloseBrace(boolean aValue);
}
