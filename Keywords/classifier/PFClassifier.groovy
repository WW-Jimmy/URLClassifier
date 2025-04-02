package classifier
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import utils.DriverManager
import com.kms.katalon.core.testobject.SelectorMethod

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class PFClassifier {
	private static final int ELEMENT_VERIFICATION_TIMEOUT = 5

	@Keyword
	def classifyBySelector(String originalUrl, String finalUrl = null) {
		// Use finalUrl if offered, or use originalUrl
		String url = finalUrl ?: originalUrl
		KeywordUtil.logInfo("Classify by PF selector" + url)

		try {
			WebDriver driver = DriverManager.getDriver()

			// Find Product Filter Container
			boolean isProductFilterPresent = WebUI.verifyElementPresent(findTestObject('PF/product_filter_container')
					, 5, FailureHandling.OPTIONAL)
			// Q. WebUI를 최대한 지양했는데, 이방법을 안쓰면 CSS를 직접 따와서 넣는 불필요한 방법을 쓰던데 WebUI를 써도되는지...?

			if (isProductFilterPresent) {
				KeywordUtil.logInfo("Found PF Filter Container, Classified as PF")
				return "PF"
			} else {
				KeywordUtil.logInfo("Cannot Find PF Container")
			}

			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error Occurred while Classifying PF: " + e.toString())
			return "Go to Next Step"
		}
	}
}