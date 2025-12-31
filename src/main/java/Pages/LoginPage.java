package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {
    private final By emailField = By.xpath("//input[@type='email']");
    private final By passwordField = By.xpath("//input[@type='password']");
    private final By loginButton = By.xpath("//button[@type='submit']");
    private final By errorMessage = By.xpath("//*[@id=\"root\"]/div/main/div/div[3]/div[3]");



    // Setters
    public void setEmail(String email) {
        setText(emailField, email);
    }
    public void setPassword(String password) {
        setText(passwordField, password);
    }
    // Transition methods
    public DashboardPage clickLogin() {
        click(loginButton);
        return new DashboardPage();
    }
    //Getters
    public String getErrorMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(errorMessage)
        ).getText();
    }

    // Convenience method
    public DashboardPage logIntoApplication(String email, String password) {
        setEmail(email);
        setPassword(password);
        return clickLogin();
    }
    public boolean isLoginButtonVisible() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(loginButton)
        ).isDisplayed();
    }

}

