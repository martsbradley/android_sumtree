package com.mbradley.sumtree.view;

import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.mbradley.sumtree.sumdisplay.android.SumDisplayImpl;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EndToEndTest {

    @Rule
    public ActivityTestRule<CalculatorActivity> mActivityRule = new ActivityTestRule(CalculatorActivity.class);


    //  click enough times to initialise the adverts
    //  otherwise the lag from google causes failures.
    @Before
    public void setupAdverts()
    {
        int x= 5;
        while (x-- > 0)
            pressButtons("‹");
    }

    @Test
    public void click_divide_by_zero_click_equals()
    {
        pressButtons("1/0");

        clickTreeAt(0.2f, 0.1f);

        divideByZeroMessage();
    }

    @Test
    public void clicking_root_node()
    {
        pressButtons("1+2+7");

        clickTreeAt(0.3f, 0.1f);
        checkResultIs("10");
    }

    @Test
    public void click_expression_not_ready_mid_tree()
    {
        pressButtons("1+2/");

        clickTreeAt(0.3f, 0.35f);

        calculationMissingANumber();
    }
    @Test
    public void pressing_equals_button()
    {
        pressButtons("2*3*7=");
        checkResultIs("42");
    }
    @Test
    public void one_add_two_is_three()
    {
        pressButtons("1+2=");
        checkResultIs("3");
    }

    @Test
    public void divide_by_zero_press_equals()
    {
        pressButtons("1/0=");

        divideByZeroMessage();
    }


    @Test
    public void click_divide_by_zero_mid_tree()
    {
        pressButtons("1+2/0");

        clickTreeAt(0.3f, 0.3f);

        divideByZeroMessage();
    }



    @Test
    public void expression_not_ready_equals()
    {
        pressButtons("1/=");

        calculationMissingANumber();
    }


    @Test
    public void expression_missing_closing_brace()
    {
        pressButtons("(1-2=");

        calculationMissingClosingBracket();
    }
    @Test
    public void divide_by_zero_crash()
    {
        pressButtons("10/(9/3-3)=");

    }



    /** This was causing a crash because
     *  the calculator was trying to collapse
     *  this tree
     */
    @Test
    public void causes_crash()
    {
        pressButtons("1*(=");


    }
    @Test
    public void equals_on_start_causes_crash()
    {
        pressButtons("=");
    }

    private void divideByZeroMessage()
    {
        onView(withText("Divide By Zero"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }


    private void calculationMissingANumber()
    {
        onView(withText("Missing a number"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }


    private void calculationMissingClosingBracket()
    {
        onView(withText("Missing Closing Bracket"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    private void pressButtons(String aSequence)
    {

        for (char aChar : aSequence.toCharArray())
        {
            String value = Character.toString(aChar);
            pressButton(value);
        }
    }

    @NonNull
    private ViewInteraction checkResultIs(String aResult) {
        return onView(withClassName(endsWith("SumDisplayImpl"))).perform(new CheckAnswerViewAction(aResult));
    }

    @NonNull
    private ViewInteraction pressButton(String aButtonName) {
        return onView(withText(startsWith(aButtonName))).perform(click());
    }

    @NonNull
    private ViewInteraction clickTreeAt(float pctX, float pctY)
    {
        return onView(withClassName(endsWith("DrawView"))).perform(clickPercent(pctX, pctY));
    }

    public static ViewAction clickPercent(final float pctX, final float pctY)
    {
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        int w = view.getWidth();
                        int h = view.getHeight();

                        float x = w * pctX;
                        float y = h * pctY;

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }


    private class CheckAnswerViewAction implements ViewAction
    {
        private final String mResult;
        public CheckAnswerViewAction(String aResult)
        {
            mResult = aResult;
        }

        @Override
        public Matcher<View> getConstraints() {
//            return ViewMatchers.withClassName(is("SumDisplayImpl"));
            return ViewMatchers.isEnabled();
        }

        @Override
        public String getDescription() {
            return "MyViewAction desc";
        }

        @Override
        public void perform(UiController uiController, View view)
        {
            SumDisplayImpl impl = (SumDisplayImpl)view;
            String text = impl.getText();
            assertEquals(mResult, text);
        }
    }
}
