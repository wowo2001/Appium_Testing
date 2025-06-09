package com.example.com.hooks;

import com.example.com.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.Before;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;


public class Hooks {
    private static AndroidDriver driver;

    @Before
    public void setUp() throws Exception {
        System.out.println("***** Setup in Hooks ******");
        driver = DriverFactory.createDriver();
        ThucydidesWebDriverSupport.useDriver(driver);
    }



}
