package com.mbradley.sumtree.parser;

import com.mbradley.sumtree.layout.shape.AddOperation;
import com.mbradley.sumtree.layout.shape.BraceOperation;
import com.mbradley.sumtree.layout.shape.DivideOperation;
import com.mbradley.sumtree.layout.shape.IntegerOperation;
import com.mbradley.sumtree.layout.shape.MultiplyOperation;
import com.mbradley.sumtree.layout.shape.Operation;
import com.mbradley.sumtree.layout.shape.PowerOperation;
import com.mbradley.sumtree.layout.shape.SubOperation;
import com.mbradley.sumtree.layout.shape.visitor.UnaryMinusOperation;


public class IncompleteSumParser
{
    private boolean mUnaryMinus = false;
    private int pos = -1, ch;
    private final String str;
//    public static char FANCY_MINUS = '–';
    public IncompleteSumParser(String aExpr)
    {
        str = aExpr;
    }

    public Operation parse()
    {
        nextChar();
        Operation x = parseExpression();
        if (pos < str.length())
            throw new RuntimeException("Unexpected: " + (char) ch);

        if (x == null)
        {
            x = new UnaryMinusOperation();
        }
        return x;
    }


    private void nextChar()
    {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(int charToEat)
    {
        while (ch == ' ')
            nextChar();
        if (ch == charToEat)
        {
            nextChar();
            return true;
        }
        return false;
    }
//    private boolean peek(int charToPeek)
//    {
//        int localPos= pos;
//
//        int found = (++localPos < str.length()) ? str.charAt(localPos) : -1;
//        return found == charToPeek;
//
//    }



    // Grammar:
    // expression = term
    //             |expression `+` term
    //             |expression `-` term
    //
    // term =       factor
    //             |term `*` factor
    //             |term `/` factor
    //
    // factor =     `(` expression `)`
    //             | number
    //             | factor `^` factor



    private Operation parseExpression()
    {
        checkUnaryMinus();

        Operation expr = parseTerm();

        for (; ; )
        {
            if (eat('+'))                    // addition
            {
                Operation newExpr = new AddOperation();
                if (expr == null) throw new RuntimeException("Left child cannot be null");
                newExpr.setChild(expr);
                checkUnaryMinus();
                Operation y =  parseTerm();
                newExpr.setChild(y);
                expr = newExpr;


            }
            else if (eat('-'))               // subtraction
            {
                Operation newExpr = new SubOperation();
                if (expr == null) throw new RuntimeException("Left child cannot be null");
                newExpr.setChild(expr);
                checkUnaryMinus();
                Operation y = parseTerm();
                newExpr.setChild(y);
                expr = newExpr;


            }
            else
            {
                break;
            }
        }
        return expr;
    }

    private Operation parseTerm()
    {
        Operation result = parseFactor();
        Operation expr2;

        for (; ; )
        {
            if (eat('*'))
            {
                Operation newExpr = new MultiplyOperation();
                if (result == null) throw new RuntimeException("Left child cannot be null");
                newExpr.setChild(result);
                checkUnaryMinus();
                Operation y = parseFactor();
                newExpr.setChild(y);
                result = newExpr;
            }
            //÷
            //else if (eat('/'))
            else if (eat('÷'))
            {
                if (result == null) throw new RuntimeException("Left child cannot be null");
                final Operation divide = new DivideOperation();
                divide.setChild(result);
                checkUnaryMinus();
                Operation y = parseFactor();
                divide.setChild(y);
                result = divide;
            }
            else if ((expr2 = parseFactor()) != null)
            {
                Operation newExpr = new MultiplyOperation();
                newExpr.setChild(result);
                newExpr.setChild(expr2);
                result = newExpr;
            }
            else
            {
                break;
            }
        }
        return result;
    }

    // factor =     `(` expression `)`
    //             | number
    //             | factor `^` factor
    private Operation parseFactor()
    {
        Operation x = null;
        int startPos = this.pos;

        if (eat('('))         // parentheses
        {
            x  = new BraceOperation();

            Operation expr = parseExpression();

            if (expr != null)
            {
                expr.setOpenBrace(true);

                if (eat(')'))
                {
                    if (!(expr instanceof IntegerOperation) &&
                          expr.getRightChild() == null)
                    {
                        //  Prevent (9*) where the expression inside braces is incomplete.

                        throw new RuntimeException("Right child cannot be null");
                    }

                    expr.setCloseBrace(true);
                }
                x = expr;
            }
        }
        else if ((ch >= '0' && ch <= '9') || ch == '.')
        {
            // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.')
                nextChar();

            String substring = str.substring(startPos, this.pos);

            if (mUnaryMinus)
            {
                substring = "-" + substring;
            }
            x = new IntegerOperation(substring);
        }


        if (x != null && eat('^'))
        {
            final Operation power = new PowerOperation();
            power.setChild(x);
            checkUnaryMinus();
            Operation y = parseFactor();
            power.setChild(y);
            x = power;
        }

        return x;
    }

    /**
     * The difference between (prefix) unary and (infix) binary operators is
     * the context in which they occur. A binary operator always follows an expression,
     * while a unary operator occurs at a position where an expression is expected,
     * i.e. at the start, after an operator or after an opening parenthesis.
     */
    private void checkUnaryMinus()
    {
        mUnaryMinus = ch == '-';

        if (mUnaryMinus)
        {
            nextChar();
        }
    }
}
