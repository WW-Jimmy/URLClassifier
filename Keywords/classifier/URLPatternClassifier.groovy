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

		// Convert URL to lowercase for consistent matching
		url = url.toLowerCase()

		try {
			// Basic URL validation
			if (!url.contains("samsung.com/")) {
				KeywordUtil.logInfo("Not a Samsung URL: " + url)
				return "Go to Next Step"
			}

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

			// Step 7: Check for PCD (Product Category Detail) page
			if (URLUtils.isPCDPage(url)) {
				KeywordUtil.logInfo("Classified as PCD Page: " + url)
				return "PCD"
			}

			// No matching pattern found
			KeywordUtil.logInfo("Could not classify URL pattern: " + url)
			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred during URL pattern classification: " + e.toString())
			return "Go to Next Step"
		}
	}

	private boolean isHomePage(String url) {
		String countryCode = URLUtils.extractCountryCode(url)
		String afterDomain = URLUtils.extractDomainSuffix(url)

		// Check if domain suffix matches country code (with or without trailing slash)
		if (afterDomain.equals(countryCode.toLowerCase() + "/") ||
				afterDomain.equals(countryCode.toLowerCase())) {
			return true
		}

		// Check for business homepage
		if (afterDomain.equals(countryCode.toLowerCase() + "/business/") ||
				afterDomain.equals(countryCode.toLowerCase() + "/business")) {
			return true
		}

		return false
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

		// Check for exact match of mobile page
		if (afterDomain.equals(countryCode.toLowerCase() + "/mobile/") ||
				afterDomain.equals(countryCode.toLowerCase() + "/mobile")) {
			return true
		}

		// Check for exact match of home appliances page
		if (afterDomain.equals(countryCode.toLowerCase() + "/home-appliances/") ||
				afterDomain.equals(countryCode.toLowerCase() + "/home-appliances")) {
			return true
		}

		return false
	}
}
