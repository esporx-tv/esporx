package tv.esporx.ui.pages;

import org.fluentlenium.core.FluentPage;

public class RegisterPage extends FluentPage {

    @Override
    public String getUrl() {
        return "/user/register";
    }

    public void register(String login, String password) {
        fill("#email").with(login);
        fill("#password").with(password);
        fill("#passwordConfirmation").with(password);
        click("input[type=submit]");
    }
}
