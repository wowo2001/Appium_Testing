package com.example.com;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;


import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {
    public static AndroidDriver createDriver() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setDeviceName("Android Emulator");
        options.setPlatformVersion("16.0");
        options.setAutomationName("UiAutomator2");
        options.setApp("C:\\Users\\lizhe\\Downloads\\app-alpha-universal-release.apk");
        options.setCapability("noSign", true);
        options.setAppWaitActivity("org.wikipedia.onboarding.InitialOnboardingActivity,org.wikipedia.main.MainActivity");
        AndroidDriver driver = new AndroidDriver(new URL("http://localhost:4723"), options);
        return driver;
    }
}