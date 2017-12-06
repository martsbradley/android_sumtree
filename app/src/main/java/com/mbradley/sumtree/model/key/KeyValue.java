package com.mbradley.sumtree.model.key;


public enum KeyValue
{
    ZERO       ("0", KeyType.NUMBER),
    ONE        ("1", KeyType.NUMBER),
    TWO        ("2", KeyType.NUMBER),
    THREE      ("3", KeyType.NUMBER),
    FOUR       ("4", KeyType.NUMBER),
    FIVE       ("5", KeyType.NUMBER),
    SIX        ("6", KeyType.NUMBER),
    SEVEN      ("7", KeyType.NUMBER),
    EIGHT      ("8", KeyType.NUMBER),
    NINE       ("9", KeyType.NUMBER),
    DOT        (".", KeyType.DECIMAL_PLACE),
    ADD        ("+", KeyType.OPERATOR),
    SUBTRACT   ("-", KeyType.OPERATOR),
    MULTIPLY   ("*", KeyType.OPERATOR),
    DIVIDE     ("÷", KeyType.OPERATOR),
    POWER      ("^", KeyType.OPERATOR),
    OPEN_BRACE ("(", KeyType.OPEN_BRACE),
    CLOSE_BRACE(")", KeyType.CLOSE_BRACE),
    BACK       ("",  KeyType.BACK),
    EQUALS     ("=", KeyType.EQUALS);

    private final String mText;
    private final boolean mOperator;
    private final boolean mBackSpace;
    private final boolean mDecimalPlace;
    private final boolean mEquals;

    KeyValue(String aText, KeyType aKeyType)
    {
        mText = aText;
        mOperator     = aKeyType == KeyType.OPERATOR;
        mBackSpace    = aKeyType == KeyType.BACK;
        mDecimalPlace = aKeyType == KeyType.DECIMAL_PLACE;
        mEquals       = aKeyType == KeyType.EQUALS;
    }

    public String getText()
    {
        return mText;
    }
    public boolean isOperator()
    {
        return mOperator;
    }

    public boolean isBackSpace()
    {
        return mBackSpace;
    }
    public boolean ismDecimalPlace()
    {
        return mDecimalPlace;
    }

    public boolean isEquals()
    {
        return mEquals;
    }

    private enum KeyType
    {
        NUMBER,
        OPERATOR,
        BACK,
        DECIMAL_PLACE,
        OPEN_BRACE,
        CLOSE_BRACE,
        EQUALS
    }
}
