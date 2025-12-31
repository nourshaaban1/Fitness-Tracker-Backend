package com.example.fitness_tracker.Automated_Tests;

import Pages.LoginPage;
import Pages.RegisterPage;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

public class HomeTest extends BaseTest{
    @Test
    void testRedirectToLoginPage() throws InterruptedException {
        LoginPage loginPage = homepage.redirectToLogin();
        Assertions.assertEquals(true, loginPage.isLoginButtonVisible());
    }
    @Test
    void testRedirectToRegisterPage(){
        RegisterPage  registerPage= homepage.redirectToRegister();
        Assertions.assertEquals(true, registerPage.isRegisterButtonVisible());
    }
}
