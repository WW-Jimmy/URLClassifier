package classifier

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

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
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import utils.DriverManager
import com.kms.katalon.core.testobject.SelectorMethod

public class PDClassifier {
	private static final int ELEMENT_VERIFICATION_TIMEOUT = 5

	@Keyword
	def classifyBySelector(String originalUrl, String finalUrl = null) {
		// Use finalUrl if offered, or use originalUrl
		String url = finalUrl ?: originalUrl
		KeywordUtil.logInfo("Classify by PD selector" + url)

		try {
			WebDriver driver = DriverManager.getDriver()

			// Find CTA Button
			boolean isCTAPresent = WebUI.verifyElementPresent(findTestObject('PD/product_buying_cta_button'),
					5, FailureHandling.OPTIONAL)

			if (isCTAPresent) {
				KeywordUtil.logInfo("Found Product CTA Button, Classified as PD")
				return "PD"
			} else {
				KeywordUtil.logInfo("Cannot Find PD CTA")
			}

			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error Occurred while Classifying PF: " + e.toString())
			return "Go to Next Step"
		}
	}
}
