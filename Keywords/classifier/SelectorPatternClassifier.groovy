package classifier

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import utils.URLUtils

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import internal.GlobalVariable

public class SelectorPatternClassifier {
	private static final int ELEMENT_VERIFICATION_TIMEOUT = 1

	@Keyword
	def classifyBySelector(String originalUrl, String finalUrl = null) {
		// Use finalUrl if it's offered, or use originalUrl
		String url = finalUrl ?: originalUrl
		KeywordUtil.logInfo("Starting selector classification" + url)

		try {
			URLUtils.closeAllPopups()

			String classification

			// Step 1: Try PF classification
			classification = classifyAsPF()
			if (classification != "Go to Next Step") {
				return classification
			}

			// Step 2: Try PCD classification
			classification = classifyAsPCD()
			if (classification != "Go to Next Step") {
				return classification
			}

			// Step 3: Try PD classification last
			classification = classifyAsPD()
			if (classification != "Go to Next Step") {
				return classification
			}

			// If nothing classified until here, classified as 'Unclassified'
			KeywordUtil.logInfo("Could not classify page using selectors: " + url)
			return "Unclassified"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred during selector classification: " + e.toString())
			return "Error:DOMInspection"
		}
	}

	private String classifyAsPF() {
		try {
			boolean isProductFilterPresent = WebUI.verifyElementPresent(
					findTestObject('PF/product_filter_container'),
					ELEMENT_VERIFICATION_TIMEOUT,
					FailureHandling.OPTIONAL
					)

			if (isProductFilterPresent) {
				KeywordUtil.logInfo("Found PF Filter Container, Classified as PF")
				return "PF"
			} else {
				KeywordUtil.logInfo("Cannot Find PF Container")
			}

			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred while classifying PF: " + e.toString())
			return "Error:DOMInspection"
		}
	}

	private String classifyAsPCD() {
		try {
			boolean isPCDFilterPresent = WebUI.verifyElementPresent(
					findTestObject('PCD/pcd_quick_filter_link'),
					ELEMENT_VERIFICATION_TIMEOUT,
					FailureHandling.OPTIONAL
					)

			if (isPCDFilterPresent) {
				KeywordUtil.logInfo("Found PCD Filter Container, Classified as PCD")
				return "PCD"
			} else {
				KeywordUtil.logInfo("Cannot Find PCD Container")
			}

			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred while classifying PCD: " + e.toString())
			return "Error:DOMInspection"
		}
	}

	private String classifyAsPD() {
		try {
			boolean isCTAPresent = WebUI.verifyElementPresent(
					findTestObject('PD/product_detail_header'),
					ELEMENT_VERIFICATION_TIMEOUT,
					FailureHandling.OPTIONAL
					)

			if (isCTAPresent) {
				KeywordUtil.logInfo("Found Product CTA Button, Classified as PD")
				return "PD"
			} else {
				KeywordUtil.logInfo("Cannot Find PD CTA")
			}

			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred while classifying PD: " + e.toString())
			return "Error:DOMInspection"
		}
	}
}