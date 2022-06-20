package org.audit4j.core;

import org.audit4j.core.integ.IntegTestSuite;
import org.audit4j.core.smoke.SmokeTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SmokeTestSuite.class, Audit4jCoreUnitTestSuit.class, IntegTestSuite.class })
public class Audit4jTestRunner {

}
