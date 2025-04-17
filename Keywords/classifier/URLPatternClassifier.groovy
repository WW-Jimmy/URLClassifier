package classifier

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import utils.URLUtils
import constant.SamsungURLConstants as OOS

public class URLPatternClassifier {
	@Keyword
	def classifyByURL(String url) {
		def afterDomain =  URLUtils.extractDomainSuffix(url)
		def isCN = url.contains("samsung.com.cn")
		def segments = afterDomain.split("/").findAll { it }
		def validSeg = segments.size() == (isCN ? 1 : 2)

		def first = isCN ? segments[0] : segments[1]
		def last = segments ? segments[-1] : ""

		if (first in OOS.OOS_PATHS || last == "buy" || segments.any { it.contains("buying-guide") }) 
			return "OOS"
		
		if (first == "offer")
			return "Offer"
	
		if (validSeg && first in ["mobile", "home-appliances"])
			return "PFS"
	
		if (first == "explore")
			return "Explore"
	
		if (!first && (afterDomain || isCN))
			return "Home"
		
		if (validSeg && first == "footer")
			return "Footer"
		
		return "Go to Next Step"
	}
}