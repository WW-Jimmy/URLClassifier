import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.model.FailureHandling as FailureHandling

import internal.GlobalVariable as GlobalVariable

import utils.URLUtils as URLUtils
import services.ResultsBufferService as ResultsBufferService
import services.URLCacheService as URLCacheService

try {
	def countryCode = URLUtils.extractCountryCode(url)
    def finalUrl = URLUtils.checkRedirect(url)
	def classification = URLCacheService.checkCache(finalUrl)
	
	if(!classification) {
		classification = CustomKeywords.'classifier.URLPatternClassifier.classifyByURL'(finalUrl)
		
		if (classification == 'Go to Next Step') {
			classification = CustomKeywords.'classifier.SelectorPatternClassifier.classifyBySelector'(finalUrl)
		}
		URLCacheService.addToCache(finalUrl, classification)
	}
	KeywordUtil.logInfo("🟨🟨🟨🟨🟨🟨 [최종 분류 결과] : ${classification} 🟨🟨🟨🟨🟨🟨")
	    
	ResultsBufferService.addResult(countryCode, url, finalUrl, classification)
}
catch (Exception e) {
	def countryCode = URLUtils.extractCountryCode(url)
    ResultsBufferService.addResult(countryCode, url, url, 'Error:Processing')
}