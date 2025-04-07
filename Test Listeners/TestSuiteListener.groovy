import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.WebDriver
import services.ResultsBufferService


class TestSuiteListener {
	private static final String RESULTS_FILE_PATH = GlobalVariable.resultFilePath
	
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		try {
			KeywordUtil.logInfo("Test Suite Start: Initialize result Buffer")		
			
			// Clear past Result
			ResultsBufferService.clearBuffer()
			
			// Open Browser
			WebUI.openBrowser('')
			
		} catch (Exception e) {
			KeywordUtil.markError("Error occured while Initialize Test Suite : ${e.toString()}")
				
		}
	}
	
	@AfterTestSuite
	def afterTestSuite(TestSuiteContext testSuiteContext) {
		try {
			KeywordUtil.logInfo("Testsuite Complete, Saving Results...")
			
			// Save Result to File
			ResultsBufferService.saveToFilePerCountry(RESULTS_FILE_PATH)
			
			// Count processed URL
			int processedCount = ResultsBufferService.getProcessedCount()
			KeywordUtil.logInfo("Processed URL: $processedCount")
			
			// Clear Buffer
			ResultsBufferService.clearBuffer()
			
			// Clear Browser
			WebUI.closeBrowser()
			
		} catch (Exception e) {
			KeywordUtil.markError("Error occured while finishing Test Suite : ${e.toString()}")
		}
	}
}