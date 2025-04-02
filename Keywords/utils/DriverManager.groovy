package utils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions


import internal.GlobalVariable

public class DriverManager {
	private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>()

	static WebDriver getDriver() {

		if (threadDriver.get() == null) {
			System.setProperty("webdriver.chrome.driver", GlobalVariable.chromeDriverPath)

			// Since to use Concurrent and Browser Options,
			//Use ChromeOptions, not WebUI.openBrowser('')
			ChromeOptions options = new ChromeOptions()

			// Browser Options flag (DEBUG = FALSE)
			boolean isHeadless = false

			if (isHeadless) {
				options.addArguments('--headless=new')
				options.addArguments('--window-size=1920,1080')
			}

			WebDriver driver = new ChromeDriver(options)
			threadDriver.set(driver)
			KeywordUtil.logInfo("[DriverManager] WebDriver created for thread: ${Thread.currentThread().getId()}")
		}
		return threadDriver.get()
	}

	static void quitDriver() {
		WebDriver driver = threadDriver.get()
		if (driver != null) {
			KeywordUtil.logInfo("[DriverManager] Closing WebDriver for thread: ${Thread.currentThread().getId()}")
			driver.quit()
			threadDriver.remove()
		}
	}
}