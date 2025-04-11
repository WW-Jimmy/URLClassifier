package classifier

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import utils.URLUtils
import constant.SamsungURLConstants

public class URLPatternClassifier {

	@Keyword
	def classifyByPattern(String url) {
		if (!url?.trim()) {
			KeywordUtil.logInfo("Empty URL has provided.")
			return "Go to Next Step"
		}

		url = url.toLowerCase()

		try {
			def pageTypes = [
				[check: { -> URLUtils.isOOSPage(url) }, type: "OOS"],
				[check: { -> isHomePage(url) }, type: "Home"],
				[check: { -> isFooterPage(url) }, type: "Footer"],
				[check: { -> isPathTypePage(url, "explore") }, type: "Explore"],
				[check: { -> isPathTypePage(url, "offer") }, type: "Offer"],
				[check: { -> isPFSPage(url) }, type: "PFS"]
			]

			for (def pageType : pageTypes) {
				if (pageType.check()) {
					KeywordUtil.logInfo("${pageType.type} - ${url}")
					return pageType.type
				}
			}
			return "Go to Next Step"
		} catch (Exception e) {
			KeywordUtil.markWarning("Error occurred while Classfiying with URL Pattern: ${e.message}")
			return "Error:URLInspection"
		}
	}

	private boolean isHomePage(String url) {
		if (url?.contains("samsung.com.cn")) {
			String path = URLUtils.extractDomainSuffix(url)
			boolean isHome = (path == null || path.length() == 0)
			return isHome
		}

		def afterDomain = URLUtils.extractDomainSuffix(url)
		if (!afterDomain) return false

		def countryCode = URLUtils.extractCountryCode(url).toLowerCase()
		return afterDomain == countryCode
	}

	private boolean isFooterPage(String url) {
		if (url?.contains("samsung.com.cn")) {
			String path = URLUtils.extractDomainSuffix(url)
			return path == "footer"
		}

		def afterDomain = URLUtils.extractDomainSuffix(url)
		if (!afterDomain) return false

		def countryCode = URLUtils.extractCountryCode(url).toLowerCase()
		return afterDomain == "${countryCode}/footer"
	}


	private boolean isPFSPage(String url) {
		def afterDomain = URLUtils.extractDomainSuffix(url)
		if (!afterDomain) return false

		if (url.contains("samsung.com.cn")) {
			return ["mobile", "home-appliances"].contains(afterDomain)
		}

		def countryCode = URLUtils.extractCountryCode(url).toLowerCase()
		def expectedPaths = [
			"${countryCode}/mobile",
			"${countryCode}/home-appliances"
		]

		return expectedPaths.contains(afterDomain)
	}

	private boolean isPathTypePage(String url, String pathType) {
		def afterDomain = URLUtils.extractDomainSuffix(url)
		if (!afterDomain) return false

		def parts = afterDomain.split("/")
		boolean isChina = url.contains(".com.cn")

		if (isChina) {
			return parts.size() >= 1 && parts[0].equalsIgnoreCase(pathType)
		} else {
			return parts.size() >= 2 && parts[1].equalsIgnoreCase(pathType)
		}
	}
}