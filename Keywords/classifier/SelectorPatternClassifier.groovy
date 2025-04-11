package classifier

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import utils.URLUtils

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.util.concurrent.TimeoutException

import org.openqa.selenium.support.ui.WebDriverWait

import internal.GlobalVariable

public class SelectorPatternClassifier {
	private static final def PAGE_TYPES = [
		PF: [
			selector: 'PF/product_filter_container',
			description: 'PF Filter Container'
		],
		PCD: [
			selector: 'PCD/pcd_quick_filter_link',
			description: 'PCD Filter Container'
		],
		PD: [
			selector: 'PD/product_detail_header',
			description: 'Product Detail Header'
		]
	]

	private static final def CLASSIFICATION_ORDER = ['PD', 'PF', 'PCD']

	@Keyword
	def classifyBySelector(String url) {
		try {
			URLUtils.closeAllPopups()

			def result = CLASSIFICATION_ORDER.find { type ->
				isElementDisplayed(type)
			}

			return result ?: "Unclassified"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error Occured while classifying ${e.message}")
			return "Error:DOMInspection"
		}
	}

	private boolean isElementDisplayed(String type) {
		def typeInfo = PAGE_TYPES[type]
		def testObject = findTestObject(typeInfo.selector)

		try {
			return new WebDriverWait(DriverFactory.webDriver, 3).until { driver ->
				try {
					def element = WebUI.findWebElement(testObject, 1)

					if (element.displayed) {
						KeywordUtil.logInfo("Found: ${typeInfo.description}, Classified as: ${type}")
						return true
					}
					return false
				} catch (Exception e) {
					return false
				}
			}
		} catch (TimeoutException e) {
			KeywordUtil.logInfo("Could not find: ${typeInfo.description}")
			return false
		} catch (Exception e) {
			KeywordUtil.markWarning("Error checking ${type}: ${e.message}")
			return false
		}
	}
}