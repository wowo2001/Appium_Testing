package com.example.com.interactions;

import com.example.com.pageObject.LoginPage;
import net.serenitybdd.annotations.Steps;

public class LoginInteractions extends BaseInteractions{

    @Steps
    LoginPage loginPage;

    public void clickOnNext()
    {
        waitForPageLoadWithElement(loginPage.playStoreIcon);
        swipeLeft();
        clickOn(loginPage.playStoreIcon);

    }
}
