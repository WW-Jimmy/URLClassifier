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
	Map<String, Object> redirectResult = URLUtils.checkRedirect(url)
    String finalUrl = redirectResult.finalUrl
    boolean redirected = redirectResult.redirected

    if (redirected) {
        KeywordUtil.logInfo("URL Redirected: $url -> $finalUrl")
    }
    
    // Step 2: Check if finalURL is already in cache 
	String cachedClassification = URLCacheService.checkCache(finalUrl)
	if (cachedClassification) {
		classification = cachedClassification
		KeywordUtil.logInfo("Classification from cache " + classification)
	} else {
		// Step 3: Classify By URL Pattern  
		classification = CustomKeywords.'classifier.URLPatternClassifier.classifyByPattern'(url, finalUrl)
		KeywordUtil.logInfo('URL Classification Result: ' + classification)
		
		// Step 4: DOM Check - Only If URL Classification returns "Go to Next Step"
		if (classification == 'Go to Next Step') {
			KeywordUtil.logInfo('URL Classification unclear, Check DOM Selectors')
		
			classification = CustomKeywords.'classifier.SelectorPatternClassifier.classifyBySelector'(url, finalUrl)
			KeywordUtil.logInfo("Selector Classification Result: ${classification}")
		}
		
		URLCacheService.addToCache(finalUrl, classification)
	}
	
	KeywordUtil.logInfo("🟨🟨🟨🟨🟨 [최종 분류 결과] : ${classification} 🟨🟨🟨🟨🟨")
	
    // Step 5: Save Result
	// Step 5-1: Extract Country Code
    String countryCode = URLUtils.extractCountryCode(url)

    // Step 5-2: Add Results to Buffer
    ResultsBufferService.addResult(countryCode, url, finalUrl, classification)
}
catch (Exception e) {
    KeywordUtil.markWarning("Error occured while checking URL: $url" + e.getMessage())

    String countryCode = URLUtils.extractCountryCode(url)

    ResultsBufferService.addResult(countryCode, url, url, 'Error:Processing')
}