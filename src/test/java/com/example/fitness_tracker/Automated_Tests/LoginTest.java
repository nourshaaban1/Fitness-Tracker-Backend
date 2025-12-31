package com.example.fitness_tracker.Automated_Tests;

import Pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.Sleeper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest extends BaseTest {

    @Test
    public void testLoginErrorMessage() throws InterruptedException {
        LoginPage loginPage = homepage.redirectToLogin();
        String email = "test@gmail.com";
        String password = "wrongpassword";
        loginPage.logIntoApplication(email,password);
        assertEquals("Login failed, please try again.", loginPage.getErrorMessage());
    }

}
