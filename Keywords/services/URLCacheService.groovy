package services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable

public class URLCacheService {
	// Static cache to store classified URLs
	private static final Map<String, String> urlClassificationCache = [:]

	// Cache hit counter for statistics
	private static int cacheHitCount = 0

	@Keyword
	static String checkCache(String finalUrl) {
		if (!finalUrl || finalUrl.trim().isEmpty()) {
			return null
		}

		String classification = urlClassificationCache[finalUrl]

		if (classification) {
			cacheHitCount++
			KeywordUtil.logInfo("[CACHE HIT] URL found in cache: ${finalUrl} -> ${classification}")
		}

		return classification
	}

	@Keyword
	static void addToCache(String finalUrl, String classification) {
		if(!finalUrl || finalUrl.trim().isEmpty() || !classification)
			return

		// Only cache if it doesn't already exist
		if (!urlClassificationCache.containsKey(finalUrl)) {
			urlClassificationCache[finalUrl] = classification
			KeywordUtil.logInfo("[CACHE ADD] URL added to cache: ${finalUrl} -> ${classification}")
		}
	}

	@Keyword
	static String getCacheStats() {
		int totalEntries = urlClassificationCache.size()
		return "Cache Stats: ${totalEntries} entries, ${cacheHitCount} hits"
	}

	@Keyword
	static void clearCache() {
		urlClassificationCache.clear()
		cacheHitCount = 0
		KeywordUtil.logInfo("URL Classification cache cleared")
	}
}
