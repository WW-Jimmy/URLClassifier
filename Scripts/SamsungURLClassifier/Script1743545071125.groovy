import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.model.FailureHandling as FailureHandling

import internal.GlobalVariable as GlobalVariable

import utils.URLUtils as URLUtils
import services.ResultsBufferService as ResultsBufferService
import services.URLCacheService as URLCacheService

try {
	// Step 1: Check Redirect URL to get Final URL
    def finalUrl = URLUtils.checkRedirect(url)
	
	// Step 2: Check if this URL is already in Cache 
	def classification = URLCacheService.checkCache(finalUrl)
	
	if(!classification) {
		// Step 3-1: Classify By URL Pattern  
		classification = CustomKeywords.'classifier.URLPatternClassifier.classifyByPattern'(finalUrl)
		
		// Step 3-2: DOM Check - Only If URL Classification returns "Go to Next Step"
		if (classification == 'Go to Next Step') {
			classification = CustomKeywords.'classifier.SelectorPatternClassifier.classifyBySelector'(finalUrl)
		}
		// Step 3-3: Add Cache if this URL is not saved in Cache yet
		URLCacheService.addToCache(finalUrl, classification)
	}
	KeywordUtil.logInfo("🟨🟨🟨🟨🟨🟨 [최종 분류 결과] : ${classification} 🟨🟨🟨🟨🟨🟨")
	
    // Step 4: Save Result
    def countryCode = URLUtils.extractCountryCode(url)
	ResultsBufferService.addResult(countryCode, url, finalUrl, classification)
}
catch (Exception e) {
    def countryCode = URLUtils.extractCountryCode(url)
    ResultsBufferService.addResult(countryCode, url, url, 'Error:Processing')
}