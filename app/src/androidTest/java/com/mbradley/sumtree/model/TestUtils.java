package com.mbradley.sumtree.model;

import android.app.Activity;
import android.content.Intent;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUtils
{

    public static Intent assertFinishCalledWithResult(int resultCode, Activity aActivity)
    {
        assertTrue(aActivity.isFinishing());
        try {
            Field f = Activity.class.getDeclaredField("mResultCode");
            f.setAccessible(true);
            int actualResultCode = (Integer)f.get(aActivity);
            assertEquals(resultCode, actualResultCode);
            f = Activity.class.getDeclaredField("mResultData");
            f.setAccessible(true);
            return (Intent)f.get(aActivity);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("Looks like the Android Activity class has changed it's   private fields for mResultCode or mResultData.  Time to update the reflection code.", e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
