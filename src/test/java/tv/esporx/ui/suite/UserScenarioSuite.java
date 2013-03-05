package tv.esporx.ui.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tv.esporx.ui.suite.tests.LoginTest;
import tv.esporx.ui.suite.tests.RegisterTest;

import static org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({RegisterTest.class, LoginTest.class})
public class UserScenarioSuite {
/*
 * WARNING: you must run this via Maven or after having manually started Tomcat
 */
}
