package com.example.fitness_tracker.Automated_Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.xml.sax.Locator;

public class HomePage {
    WebDriver driver;


    @BeforeEach
    void setUp() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/");
    }
    @AfterEach
    void tearDown() {
        if (driver!=null)driver.quit();
    }

    @Test
    void testHomePageRegisterButton() {
        driver.findElement(By.cssSelector("a[href='/register']")).click();
        String expected = "http://localhost:5173/register";
        String actual = driver.getCurrentUrl();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testHomePageLoginButton() {
        driver.findElement(By.cssSelector("a[href='/login']")).click();
        String expected = "http://localhost:5173/login";
        String actual = driver.getCurrentUrl();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void testHomePageGetStartedButton() {
        WebElement button = driver.findElement(By.xpath("//button[text()='Get Started']"));
        button.click();
        String expected = "http://localhost:5173/register";
        String actual = driver.getCurrentUrl();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testToggleScreenColorBetweenDarkAndLight() throws InterruptedException {
        WebElement button = driver.findElement(By.xpath("//button[@title=\"Toggle theme\"]"));
        button.click();
        Thread.sleep(2000);
        button.click();
    }
}
