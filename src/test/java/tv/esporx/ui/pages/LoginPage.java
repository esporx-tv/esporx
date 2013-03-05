package tv.esporx.ui.pages;

import org.fluentlenium.core.FluentPage;

import static org.fest.assertions.Assertions.assertThat;

public class LoginPage extends FluentPage {

    @Override
    public String getUrl() {
        return "/admin/login";
    }

    public void login(String login, String password) {
        fill("#j_username").with(login);
        fill("input[name=j_password]").with(password);
        click("input[type=submit]");
    }

}
