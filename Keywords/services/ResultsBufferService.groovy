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
		// Create Results Map
		Map<String, String> result = [
			'countryCode': countryCode,
			'originalUrl': originalUrl,
			'finalUrl': finalUrl ?: originalUrl,
			'classification': classification
		]

		resultMapByCountry[countryCode] << result
		processedCount++
	}

	static void saveToFilePerCountry(String baseDirPath) {
		if (resultMapByCountry.isEmpty()) {
			KeywordUtil.logInfo("No Results to Save.")
			return
		}

		File baseDir = new File(baseDirPath)
		baseDir.mkdirs()

		resultMapByCountry.each { countryCode, results ->
			try {
				File file = new File(baseDir, "${countryCode}_Test_result.txt")
				BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))

				bw.write("CountryCode,OriginalUrl,FinalUrl,Classification")
				bw.newLine()

				results.each { result ->
					bw.write("${result.countryCode},${result.originalUrl},${result.finalUrl},${result.classification}")
					bw.newLine()
				}

				bw.flush()
				bw.close()

				KeywordUtil.logInfo("Saved ${results.size()} results for [$countryCode] to file: ${file.absolutePath}")
			} catch (Exception e) {
				KeywordUtil.logInfo("***!!! Error saving file for [$countryCode]: ${e.toString()}")
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

