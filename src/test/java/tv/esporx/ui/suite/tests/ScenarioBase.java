package tv.esporx.ui.suite.tests;

import com.saucelabs.common.SauceOnDemandAuthentication;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.openqa.selenium.Platform.ANY;

public class ScenarioBase extends FluentTest {

    private SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("fbiville", "24eeb969-081f-4a5a-83cc-8c9e326e2e73");

    @Override
    public String getBaseUrl() {
        return "http://localhost:8080";
    }

    @Before
    public void prepare() throws MalformedURLException {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("version", "17");
        capabilities.setCapability("platform", ANY);
        RemoteWebDriver webDriver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        initFluent(webDriver);
    }

}
