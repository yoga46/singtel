package com.test.singleton;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/features", glue = { "stepDefinition" }, monochrome = true, plugin = {
		"pretty", "html:target/cucumber-reports/report.html" })

public class Runner {

	public static WebDriver driver;

	static Properties props = new Properties();

	@BeforeClass
	public static void lanuchBrowser() {
		 System.setProperty("webdriver.gecko.driver","C:\\Users\\yoga\\Browser\\geckodriver.exe");
		 driver = new FirefoxDriver();
		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\yoga\\Browser\\chromedriver.exe");
		//driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	public static WebDriver getDriver() {
		return driver;
	}

	@BeforeClass
	public static void loadProperties() throws IOException {
		FileInputStream objfile = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\test\\resources\\locators.properties");
		props.load(objfile);
	}

	public static Properties getProperties() {
		return props;
	}

	@AfterClass
	public static void closeBrowser() {
		driver.close();
	}

}
