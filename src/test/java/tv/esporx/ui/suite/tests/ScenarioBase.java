package tv.esporx.ui.suite.tests;

import org.fluentlenium.adapter.FluentTest;

public class ScenarioBase extends FluentTest {

    @Override
    public String getBaseUrl() {
        return "http://localhost:8080";
    }

}
