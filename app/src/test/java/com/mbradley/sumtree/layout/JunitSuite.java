package com.mbradley.sumtree.layout;

import com.mbradley.sumtree.layout.shape.DivideOperationTest;
import com.mbradley.sumtree.layout.shape.visitor.SumIsCompleteVisitorTest;
import com.mbradley.sumtree.parser.IncompleteSumParserTest;
import com.mbradley.sumtree.calculator.CalculatorDialogModelTest;
import com.mbradley.sumtree.layout.shape.IntegerOperationTest;
import com.mbradley.sumtree.layout.shape.SumHighlightVisitorTest;
import com.mbradley.sumtree.model.key.KeyPressOutcomeTest;
import com.mbradley.sumtree.view.treeFragment.TreeUtil;
import com.mbradley.sumtree.view.treeFragment.TreeUtilTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by martin on 01/05/16.
 */


@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    CalculatorDialogModelTest.class,
    IncompleteSumParserTest.class,
    IntegerOperationTest.class,
    SumHighlightVisitorTest.class,
    KeyPressOutcomeTest.class,
    SumIsCompleteVisitorTest.class,
    DivideOperationTest.class,
    TreeUtilTests.class,
})


public class JunitSuite
{
}
