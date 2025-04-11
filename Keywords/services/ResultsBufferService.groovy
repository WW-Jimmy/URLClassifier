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

public class ResultsBufferService {
	// Static List for Save Results
	private static final Map<String, List<Map<String, String>>> resultMapByCountry = [:].withDefault { [] }

	// Processed URL Counter
	private static int  processedCount = 0

	// Add Results to Buffer
	static synchronized void addResult(String countryCode, String originalUrl, String finalUrl, String classification) {
		def redirected = originalUrl != finalUrl ? "O" : "X"

		// Create Results Map
		def result = [
			'countryCode': countryCode,
			'originalUrl': originalUrl,
			'finalUrl': finalUrl ?: originalUrl,
			'classification': classification,
			redirected: redirected
		]

		resultMapByCountry[countryCode] << result
		processedCount++
	}

	static void saveToFilePerCountry(String baseDirPath) {
		if (resultMapByCountry.isEmpty()) {
			KeywordUtil.logInfo("No Results to Save.")
			return
		}

		new File(baseDirPath).mkdirs()

		resultMapByCountry.each { countryCode, results ->
			try {
				new File(baseDirPath, "${countryCode}_Test_result.txt").withWriter { writer ->
					writer.writeLine("CountryCode,OriginalUrl,FinalUrl,Classification,Redirected")

					results.each { result ->
						writer.writeLine("${result.countryCode},${result.originalUrl},${result.finalUrl},${result.classification},${result.redirected}")
					}
				}
				KeywordUtil.logInfo("Saved ${results.size()} results for ${countryCode} to file")
			} catch (Exception e) {
				KeywordUtil.logInfo("***!!! Error saving file for $countryCode: ${e.message}")
			}
		}
	}

	static void clearBuffer() {
		resultMapByCountry.clear()
		processedCount = 0
		KeywordUtil.logInfo("Cleared all Buffered Results...")
	}

	static int getProcessedCount() {
		return processedCount
	}
}

