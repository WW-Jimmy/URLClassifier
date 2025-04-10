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
			// Step 1: Check for out-of-scope page
			if (URLUtils.isOOSPage(url)) {
				KeywordUtil.logInfo("Classified as Out-of-Scope Page: " + url)
				return "OOS"
			}

			// Step 2: Check for home page
			if (isHomePage(url)) {
				KeywordUtil.logInfo("Classified as Home Page: " + url)
				return "Home"
			}

			// Step 3: Check for footer page
			if (isFooterPage(url)) {
				KeywordUtil.logInfo("Classified as Footer Page: " + url)
				return "Footer"
			}

			// Step 4: Check for explore page
			if (isExplorePage(url)) {
				KeywordUtil.logInfo("Classified as Explore Page: " + url)
				return "Explore"
			}

			// Step 4: Check for offer page
			if (isOfferPage(url)) {
				KeywordUtil.logInfo("Classified as Offer Page: " + url)
				return "Offer"
			}

			// Step 5: Check for PFS (Product Family Showcase) page
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
			"${countryCode.toLowerCase()}"
		]

		boolean match = expectedPaths.any { it == afterDomain }
		return match
	}

	private boolean isFooterPage(String url) {
		String afterDomain = URLUtils.extractDomainSuffix(url)
		afterDomain = afterDomain.toString().replaceAll(/\/+$/, "").trim()

		if (url.contains("samsung.com.cn")) {
			return afterDomain == "footer"
		}

		String countryCode = URLUtils.extractCountryCode(url)
		def expectedPaths = [
			"${countryCode.toLowerCase()}/footer"
		]

		boolean match = expectedPaths.any { it == afterDomain }
		return match
	}

	private boolean isPFSPage(String url) {
		String afterDomain = URLUtils.extractDomainSuffix(url)
		afterDomain = afterDomain.toString().replaceAll(/\/+$/, "").trim()

		if (url.contains("samsung.com.cn")) {
			def expectedPathsForCN = [
				"mobile",
				"home-appliances"
			]
			return expectedPathsForCN.any { it == afterDomain }
		}

		String countryCode = URLUtils.extractCountryCode(url)
		def expectedPaths = [
			"${countryCode.toLowerCase()}/mobile",
			"${countryCode.toLowerCase()}/home-appliances"
		]

		return expectedPaths.any { it == afterDomain }
	}

	private boolean isExplorePage(String url) {
		def afterDomain = URLUtils.extractDomainSuffix(url)?.replaceAll(/^\/+|\/+$/, "").trim()
		if (!afterDomain) return false

		boolean isChina = url.contains(".com.cn")
		def parts = afterDomain.split("/")

		if (isChina) {
			return parts.size() >= 1 && parts[0] == "explore"
		} else {
			return parts.size() >= 2 && parts[1] == "explore"
		}
	}

	private boolean isOfferPage(String url) {
		def afterDomain = URLUtils.extractDomainSuffix(url)?.replaceAll(/^\/+|\/+$/, "").trim()
		if (!afterDomain) return false

		boolean isChina = url.contains(".com.cn")
		def parts = afterDomain.split("/")

		if (isChina) {
			return parts.size() >= 1 && parts[0] == "offer"
		} else {
			return parts.size() >= 2 && parts[1] == "offer"
		}
	}
}