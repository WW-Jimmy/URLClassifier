package utils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.fasterxml.jackson.core.io.SegmentedStringWriter
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
import com.kms.katalon.core.util.KeywordUtil
import internal.GlobalVariable
import org.openqa.selenium.WebDriver
import java.util.concurrent.TimeUnit
import constant.SamsungURLConstants
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.JavascriptExecutor

public class URLUtils {
	@Keyword
	static String extractDomainSuffix(String url) {
		try {
			if (url?.contains("samsung.com.cn/")) {
				// Only for CN Case
				return "cn/" + url.split("samsung.com.cn/")[1]
			} else if (url?.contains("samsung.com/")) {
				return url.split("samsung.com/")[1]
			}
		} catch (Exception e) {
			KeywordUtil.markWarning("Failed to Extract Domain Suffix: " + e.getMessage())
		}
		return ""
	}

	@Keyword
	static String extractCountryCode(String url) {
		try {
			String afterDomain = extractDomainSuffix(url)
			if (afterDomain) {
				String countryCode = afterDomain.split("/")[0]
				if (countryCode?.trim()) {
					return countryCode.toUpperCase()
				}
			}
		} catch (Exception e) {
			KeywordUtil.markWarning("Failed to Extract Country Code: " + e.getMessage())
		}
		return "UNKNOWN"
	}

	@Keyword
	static Map<String, Object> checkRedirect(String url, WebDriver driver){
		String finalUrl = url
		boolean redirected = false

		try {
			// Verify if URL is valid
			if (!url?.trim()) {
				KeywordUtil.logInfo("Empty URL")
				return [finalUrl: finalUrl, redirected: redirected]
			}

			if (driver == null) {
				throw new IllegalArgumentException("WebDriver must be provided in concurrent context.")
			}

			// URL Navigate
			KeywordUtil.logInfo("Open URL : " + url)
			driver.navigate().to(url)

			// Check Redirect
			String currentUrl = driver.getCurrentUrl()
			if (currentUrl && currentUrl != url) {
				finalUrl = currentUrl
				redirected = true
				KeywordUtil.logInfo("Redirect: " + url + " -> " + finalUrl)
			}
		} catch (Exception e) {
			KeywordUtil.markWarning("Redirect check error: " + e.message)
		}

		return [finalUrl: finalUrl, redirected: redirected]
	}

	@Keyword
	static boolean isPCDPage(String url) {
		try {
			String afterDomain = extractDomainSuffix(url)
			if (afterDomain.endsWith("/")) {
				afterDomain = afterDomain.substring(0, afterDomain.length() - 1)
			}

			// Divide with "/" to inspect Path Segment
			List<String> segments = []
			for (String segment :afterDomain.split("/")) {
				if (segment?.trim()) {
					segments.add(segment.toLowerCase().trim())
				}
			}

			// PCD have Only exact '2' segment (CountryCode, and Category)
			if (segments.size() == 3 && segments[1] == "business") {
			    String category = segments[2]
			    return SamsungURLConstants.PRODUCT_CATEGORIES.contains(category)
			}
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occured while checking PCD page: " + e.getMessage())
		}

		return false
	}

	@Keyword
	static boolean isOOSPage(String url) {
		try {
			for (String pathIdentifier : SamsungURLConstants.OOS_PATHS) {
				if (url.contains("/" + pathIdentifier + "/")) {
					return true
				}
			}
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occured while checking OOS page: " + e.getMessage())
		}
	}
}