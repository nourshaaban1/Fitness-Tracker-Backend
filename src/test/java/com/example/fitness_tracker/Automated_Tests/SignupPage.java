package com.example.fitness_tracker.Automated_Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class SignupPage {
    WebDriver driver;
    String dashboard =  "http://localhost:5173/dashboard";
    String login =  "http://localhost:5173/login";
    String register =  "http://localhost:5173/register";
    String home =  "http://localhost:5173/";

    @BeforeEach
    void setUp() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/register");
    }
    @AfterEach
    void tearDown() {
        if (driver!=null)driver.quit();
    }

    @Test
    void signupTest() throws InterruptedException {
        Thread.sleep(2000);
        WebElement firstName = driver.findElement(By.name("firstName"));
        WebElement lastName = driver.findElement(By.name("lastName"));
        WebElement email = driver.findElement(By.name("email"));
        WebElement password = driver.findElement(By.name("password"));
        WebElement signupButton = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
        firstName.sendKeys("John");
        lastName.sendKeys("Smith");

        email.sendKeys("john.smith"+((int)(Math.floor(Math.random()*100000))+"@gmail.com"));
        password.sendKeys("secret");
        signupButton.click();
        Thread.sleep(4000);
        Assertions.assertEquals(driver.getCurrentUrl(),dashboard);
    }

    @Test
    void testHomePageRegisterButton() {
        driver.findElement(By.cssSelector("a[href='/register']")).click();
        String expected = "http://localhost:5173/register";
        String actual = driver.getCurrentUrl();
        Assertions.assertEquals(expected, actual);
    }

    //Link under the form data test
    @Test
    void testLogin() throws InterruptedException {
        WebElement loginLink = driver.findElement(
                By.xpath("//div[contains(., 'Already have an account')]/a[@href='/login']")
        );
        Thread.sleep(2000);
        loginLink.click();
        Assertions.assertEquals(driver.getCurrentUrl(),login);
    }

    //link in navbar
    @Test
    void testLogin2() throws InterruptedException {
        WebElement button = driver.findElement(By.linkText("Login"));
        button.click();
        Thread.sleep(2000);
        Assertions.assertEquals(driver.getCurrentUrl(),login);
    }

    @Test
    void testBackToWelcomeButton() throws InterruptedException {
        WebElement backButton = driver.findElement(By.xpath("//button[normalize-space()='Back to Welcome']"));
        Actions actions = new Actions(driver);
        actions.scrollToElement(backButton).perform();
        backButton.click();
        Assertions.assertEquals(driver.getCurrentUrl(),home);
    }

}
