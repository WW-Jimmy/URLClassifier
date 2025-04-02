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
	private static final List<Map<String, String>> resultBuffer = []

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

		resultBuffer.add(result)
		processedCount++
	}

	static void saveToFile(String filePath) {
		if (resultBuffer.isEmpty()) {
			KeywordUtil.logInfo("No Results for Save")
			return
		}

		try {
			// Create Directories if needed
			File file = new File(filePath)
			file.getParentFile()?.mkdirs()


			// Ready to Write File
			FileWriter fw = new FileWriter(file, false)
			BufferedWriter bw = new BufferedWriter(fw)

			// Write Header
			bw.write("CountryCode, OriginalUrl, FinalUrl, Classification")
			bw.newLine()

			// Write All Results
			int count = 0
			resultBuffer.each { result ->
				String line = "${result.countryCode}, ${result.originalUrl}, ${result.finalUrl}, ${result.classification}"
				bw.write(line)
				bw.newLine()
				count++
			}

			// Close File
			bw.flush()
			bw.close()
			fw.close()

			KeywordUtil.logInfo("Successed to Save Results: $count, File: ${filePath}")
		} catch (Exception e) {
			KeywordUtil.logInfo("Error Occured while saving results" + e.toString())
		}
	}

	static void clearBuffer() {
		resultBuffer.clear()
		processedCount = 0
		KeywordUtil.logInfo("Cleared ResultBuffer")
	}

	static int getProcessedCount() {
		return processedCount
	}
}
