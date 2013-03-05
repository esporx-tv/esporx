package tv.esporx.ui.suite.tests;

import org.fluentlenium.core.annotation.Page;
import org.junit.Before;
import org.junit.Test;
import tv.esporx.ui.pages.LoginPage;

import static org.fest.assertions.Assertions.assertThat;


/*
 * WARNING: you must run this via Maven or after having manually started Tomcat
 */
public class LoginTest extends ScenarioBase {

    @Page
    private LoginPage loginPage;

    @Before
    public void prepare() {
        goTo(loginPage);
    }

    @Test
    public void should_not_log_in_nonexistent_account() {
        loginPage.login("tototo", "toto");
        assertAt(loginPage);
    }

    @Test
    public void should_not_log_in_unconfirmed_account() {
        loginPage.login("selenium@esporx.com", "random");
        assertAt(loginPage);
        assertThat(findFirst("#reason").getText().trim()).isEqualTo("User is disabled");
    }

}
