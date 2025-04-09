package classifier

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import utils.URLUtils
import constant.SamsungURLConstants
import internal.GlobalVariable as GlobalVariable

public class URLPatternClassifier {

	@Keyword
	def classifyByPattern(String originalUrl, String finalUrl = null) {
		// Use finalUrl if provided, otherwise fallback to originalUrl
		String url = finalUrl ?: originalUrl

		// Skip if the URL is empty
		if (!url || url.trim().isEmpty()) {
			KeywordUtil.logInfo("An empty URL was provided.")
			return "Go to Next Step"
		}

		// Convert URL to lower-case for consistent matching
		url = url.toLowerCase()

		try {
			// Step 1: Check for home page
			if (isHomePage(url)) {
				KeywordUtil.logInfo("Classified as Home Page: " + url)
				return "Home"
			}

			// Step 2: Check for footer page
			if (isFooterPage(url)) {
				KeywordUtil.logInfo("Classified as Footer Page: " + url)
				return "Footer"
			}

			// Step 3: Check for explore page
			if (isExplorePage(url)) {
				KeywordUtil.logInfo("Classified as Explore Page: " + url)
				return "Explore"
			}

			// Step 4: Check for offer page
			if (isOfferPage(url)) {
				KeywordUtil.logInfo("Classified as Offer Page: " + url)
				return "Offer"
			}

			// Step 5: Check for out-of-scope page
			if (URLUtils.isOOSPage(url)) {
				KeywordUtil.logInfo("Classified as Out-of-Scope Page: " + url)
				return "OOS"
			}

			// Step 6: Check for PFS (Product Family Showcase) page
			if (isPFSPage(url)) {
				KeywordUtil.logInfo("Classified as PFS Page: " + url)
				return "PFS"
			}

			// No matching pattern found
			KeywordUtil.logInfo("Could not classify URL pattern: " + url)
			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred during URL pattern classification: " + e.toString())
			return "Error:URLInspection"
		}
	}

	private boolean isHomePage(String url) {
		String afterDomain = URLUtils.extractDomainSuffix(url)
		afterDomain = afterDomain.toString().replaceAll(/\/+$/, "").trim()

		if (url.contains("samsung.com.cn")) {
			return afterDomain == ""
		}

		String countryCode = URLUtils.extractCountryCode(url)
		def expectedPaths = [
			"${countryCode.toLowerCase()}".toString()
		]

		boolean match = expectedPaths.any { it == afterDomain }
		return match
	}

	private boolean isFooterPage(String url) {
		return url.contains("/footer/")
	}

	private boolean isExplorePage(String url) {
		return url.contains("/explore/")
	}


	private boolean isOfferPage(String url) {
		return url.contains("/offer/")
	}

	private boolean isPFSPage(String url) {
		String countryCode = URLUtils.extractCountryCode(url)
		String afterDomain = URLUtils.extractDomainSuffix(url)

		afterDomain = afterDomain.toString().replaceAll(/\/+$/, "").trim()

		def expectedPaths = [
			"${countryCode.toLowerCase()}/mobile".toString(),
			"${countryCode.toLowerCase()}/home-appliances".toString(),

			// for CN
			"mobile",
			"home-appliances",
		]

		boolean match = expectedPaths.any { it.toString() == afterDomain }
		return match
	}
}
