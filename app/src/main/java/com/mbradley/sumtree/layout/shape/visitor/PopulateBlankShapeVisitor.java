package com.mbradley.sumtree.layout.shape.visitor;

import com.mbradley.sumtree.layout.shape.BlankOperation;
import com.mbradley.sumtree.layout.shape.Shape;

/** 
 *
 * Since the antlr4 parser is going to let me
 * create expressions like '1+' where the second
 * operator is missing,
 * Need to walk the tree and fill in any null
 * right hand children.
 *
 */
public class PopulateBlankShapeVisitor implements Visitor
{
    @Override
    public void visit(Shape type)
    {
        if (type.isParent())
        {

            visit(type.getLeftChild());

            if (type.getRightChild() == null)
            {
                type.setChild(new BlankOperation());
            }
            else
            {
                visit(type.getRightChild());
            }
        }
    }
}
