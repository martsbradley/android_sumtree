package com.mbradley.sumtree.view.game;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.widget.TextView;

import com.mbradley.sumtree.model.SumtreeParser;
import com.mbradley.sumtree.model.TreeLayout;
import com.mbradley.sumtree.model.key.ParseResult;
import com.mbradley.sumtree.view.R;
//import com.mbradley.sumtree.view.TestFragmentActivity;

import java.beans.PropertyChangeEvent;

public class GameQuestionFragmentTest /*extends ActivityInstrumentationTestCase2<TestFragmentActivity>*/
{
//    private TestFragmentActivity mActivity;
//
//    public GameQuestionFragmentTest() {
//        super(TestFragmentActivity.class);
//    }
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        mActivity = getActivity();
//    }
//
//    private Fragment startFragment(Fragment fragment)
//    {
//        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.activity_test_fragment_linearlayout, fragment, "tag");
//        transaction.commit();
//        getInstrumentation().waitForIdleSync();
//        Fragment frag = mActivity.getSupportFragmentManager().findFragmentByTag("tag");
//        return frag;
//    }
//
//    public void testFragment()
//    {
//        final GameQuestionFragment fragment = new GameQuestionFragment()
//        {
//            //Override methods and add assertations here.
//
//            public void updateText(TextView aTextView, String aValue)
//            {
//                assertEquals("55", aValue);
//                super.updateText(aTextView, aValue);
//            }
//        };
//
//        Fragment frag = startFragment(fragment);
//        Object source = "the Model";
//        Object oldValue = null;
//        Object newValue = "55";
//        final PropertyChangeEvent event = new PropertyChangeEvent(source,
//                                                            GameModel.PROP_ANSWER,
//                                                            oldValue,
//                                                            newValue);
//        mActivity.runOnUiThread(
//            new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    fragment.propertyChange(event);
//                }
//            }
//        );
//    }
}
