package Pages;

import org.openqa.selenium.By;

public class HomePage  extends BasePage{
    private final By loginButton = By.cssSelector("a[href='/login']");
    private final By registerButton = By.cssSelector("a[href='/register']");
    public LoginPage redirectToLogin() {
        click(loginButton);
        return new LoginPage();
    }
    public RegisterPage redirectToRegister() {
        click(registerButton);
        return new RegisterPage();
    }
}
