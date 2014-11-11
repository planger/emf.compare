package org.eclipse.emf.compare.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	TwoWayFuzzyTest.class, 
	NodesTwoWayFuzzyTest.class,
	NodesMultiResourceTwoWayFuzzyTest.class,
	NodesIdentityTwoWayFuzzyTest.class,
	NodesResourceTwoWayFuzzyTest.class,
	MultiResourceTwoWayFuzzyTest.class,
	NodesThreeWayFuzzyTest.class})
public class AllFuzzyTests {

}