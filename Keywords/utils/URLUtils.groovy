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
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.By

public class URLUtils {
	@Keyword
	static String trimSlashes(String path) {
		if (path == null || path.isEmpty()) return ""

		def result = path
		while (result.startsWith('/')) result = result.substring(1)
		while (result.endsWith('/')) result = result[0..-2]

		return result
	}

	@Keyword
	static String extractDomainSuffix(String url) {
		try {
			if (!url?.trim()) return ""
			String result = ""

			if (url.contains("samsung.com.cn")) {
				result = url.split("samsung.com.cn", 2)[1]
			} else if (url.contains("samsung.com")) {
				result = url.split("samsung.com", 2)[1]
			} else {
				return ""
			}
			return trimSlashes(result)
		} catch (Exception e) {
			KeywordUtil.markWarning("Failed to extract Domain Suffix: " + e.getMessage())
			return ""
		}
	}

	@Keyword
	static String extractCountryCode(String url) {
		try {
			// Special Case for CN
			if (url?.contains("samsung.com.cn")) return "CN"

			// Normal Case for others
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
	static String checkRedirect(String url){
		String finalUrl = url

		try {
			// Verify if URL is valid
			if (!url?.trim()) {
				KeywordUtil.logInfo("Empty URL")
				return finalUrl
			}

			// URL Navigate
			KeywordUtil.logInfo("Open URL : " + url)
			WebUI.navigateToUrl(url)
			WebUI.waitForPageLoad(5)

			// Check Redirect
			def currentUrl = WebUI.getUrl()

			if (currentUrl && currentUrl != url) {
				finalUrl = currentUrl
				KeywordUtil.logInfo("URL Redirected: $url -> $finalUrl")
			}
		} catch (Exception e) {
			KeywordUtil.markWarning("Redirect check error: " + e.message)
		}
		return finalUrl
	}

	@Keyword
	static boolean isOOSPage(String url) {
		try {
			// Exception: only /buy/ located at the end of the segments
			if (url =~ /.*\/buy\/?$/) return true

			def afterDomain = extractDomainSuffix(url)
			if (!afterDomain?.trim()) return false

			def parts = afterDomain.split("/")

			def isChina = url.contains("samsung.com.cn")
			def result = isChina ?
					(parts.size() >= 1 && SamsungURLConstants.OOS_PATHS.contains(parts[0])) :
					(parts.size() >= 2 && SamsungURLConstants.OOS_PATHS.contains(parts[1]))

			return result
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred while checking OOS page: ${e.message}")
		}
		return false
	}

	private static boolean popupClosed = false

	@Keyword
	static void closeAllPopups() {
		if (popupClosed) {
			KeywordUtil.logInfo("Already Closed Pop-up -> Skip")
			return
		}

		try {
			String css = "#truste-consent-button, #privacyBtn, [data-an-la='cookie bar:accept'], [an-ac='cookie bar:accept'],"
			css += "#preferenceCheckBtn, .cta--black.login-leave-btn"
			boolean found = false
			new WebDriverWait(DriverFactory.webDriver, 3, 1000).ignoring(Exception.class).until({ driver ->
				def elms = driver.findElements(By.cssSelector(css)).findAll({ it.displayed })
				if (elms.size()) {
					def terms = driver.findElements(By.cssSelector("#privacy-terms, #privacy-terms2"))
					WebUI.executeJavaScript("for (const e of arguments) e.click()", terms)
					WebUI.executeJavaScript("for (const e of arguments) e.click()", elms)
					WebUI.executeJavaScript('a=document.createElement("style");a.innerHTML="iframe.fpw-view, #spr-live-chat-app {display:none !important}";document.head.appendChild(a)', null)
					found = true
					return false
				}
				return found
			})

			if (found) {
				popupClosed = true
				KeywordUtil.logInfo("Closed pop-up -> Will Skip afterwards")
			}
		} catch (Exception e) {
			KeywordUtil.markWarning("Unable to close all popup: " + e.message)
		}
	}
}