package com.example.fitness_tracker.Automated_Tests;


import Pages.BasePage;
import Pages.HomePage;
import Pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseTest {
    protected WebDriver driver;
    protected BasePage basepage;
    private String url = "http://localhost:5173/";
    protected HomePage homepage;
    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        basepage = new BasePage() {};
        basepage.setDriver(driver);
        homepage = new HomePage();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
