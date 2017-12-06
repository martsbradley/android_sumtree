package com.mbradley.sumtree.model.key;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeyPressOutcomeTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEqualsConstructor()
    {
        String e = "1+2";
        KeyPressOutcome outcome = KeyPressOutcome.equals(e);

        assertTrue(outcome.equalsWasPressed());
        assertEquals(e, outcome.getExpression());
        assertFalse(e, outcome.expressionUpdated());
    }
    @Test
    public void testNoChangeConstructor()
    {
        String e = "1+2";
        KeyPressOutcome outcome = KeyPressOutcome.noChange();

        assertFalse(outcome.equalsWasPressed());

        assertFalse(e, outcome.expressionUpdated());
    }

    @Test
    public void testNoChange_NoExpression()
    {
        thrown.expect(IllegalStateException.class);
        KeyPressOutcome outcome = KeyPressOutcome.noChange();
        outcome.getExpression();
    }
}
