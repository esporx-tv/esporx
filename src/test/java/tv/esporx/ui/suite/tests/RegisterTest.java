package tv.esporx.ui.suite.tests;

import org.fluentlenium.core.annotation.Page;
import org.junit.Before;
import org.junit.Test;
import tv.esporx.ui.pages.LoginPage;
import tv.esporx.ui.pages.RegisterPage;

import static org.fest.assertions.Assertions.assertThat;

/*
 * WARNING: you must run this via Maven or after having manually started Tomcat
 */
public class RegisterTest extends ScenarioBase {

    @Page
    private RegisterPage registerPage;
    @Page
    private LoginPage loginPage;

    @Before
    public void prepare() {
        goTo(registerPage);
    }

    @Test
    public void should_register() {
        registerPage.register("selenium@esporx.com", "random");
        assertAt(loginPage);
        assertThat(findFirst("p strong").getText()).isEqualTo("Please first confirm your account: a confirmation email has been sent.");
    }

}
