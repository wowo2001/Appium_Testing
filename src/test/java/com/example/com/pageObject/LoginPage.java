package com.example.com.pageObject;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"org.wikipedia.alpha:id/fragment_onboarding_forward_button\"]")
    public WebElement playStoreIcon;

    public LoginPage() {
        WebDriver driver = ThucydidesWebDriverSupport.getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
}
