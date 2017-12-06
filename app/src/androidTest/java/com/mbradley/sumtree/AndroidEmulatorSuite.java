package com.mbradley.sumtree;

import com.mbradley.sumtree.layout.shape.visitor.OverlapPreventionVisitorTest;
import com.mbradley.sumtree.model.KeyPressThreadTest;
import com.mbradley.sumtree.view.EndToEndTest;
import com.mbradley.sumtree.view.game.GameActivityTest;

import org.junit.runner.RunWith;

/**
 * Created by martin on 01/05/16.
 */


@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses(
{
    KeyPressThreadTest.class,
    EndToEndTest.class,
    OverlapPreventionVisitorTest.class
    //GameActivityTest.class
})


public class AndroidEmulatorSuite
{
}