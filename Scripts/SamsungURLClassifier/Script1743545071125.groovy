import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import utils.URLUtils as URLUtils
import services.ResultsBufferService as ResultsBufferService
import org.openqa.selenium.WebDriver as WebDriver
import classifier.URLPatternClassifier as URLPatternClassifier
import classifier.PFClassifier as PFClassifier
import utils.DriverManager as DriverManager

try {
    // Create Driver Instance from DriverManager (for concurrent)
    WebDriver driver = DriverManager.getDriver()

    // Step 0: Check Redirect URL and get Final URL
    Map<String, Object> redirectResult = URLUtils.checkRedirect(url, driver)

    String finalUrl = redirectResult.finalUrl

    boolean redirected = redirectResult.redirected

    if (redirected) {
        KeywordUtil.logInfo("URL Redirected: $url -> $finalUrl")
    }
    
    // Step 1: Classify By URL Pattern
    classification = CustomKeywords.'classifier.URLPatternClassifier.classifyByPattern'(url, finalUrl)

    KeywordUtil.logInfo('URL Classify Result: ' + classification)

    // Step 2: DOM Check - Only If URL Classification returns "Go to Next Step"
    if (classification == 'Go to Next Step') {
        try {
            KeywordUtil.logInfo('URL Classify unclear, Check DOM')

            // Step 2-1: Call PFClassifier
            classification = CustomKeywords.'classifier.PFClassifier.classifyBySelector'(finalUrl)
            KeywordUtil.logInfo('PF Classification Result: ' + classification)

            // Step 2-2: Call PD Classifier
            if (classification == 'Go to Next Step') {
                classification = CustomKeywords.'classifier.PDClassifier.classifyBySelector'(finalUrl)

                KeywordUtil.logInfo('PD Classification Result: ' + classification)

                if (classification == 'Go to Next Step') {
                    classification = 'Unclassified'

                    KeywordUtil.logInfo('Final Result : Unclassified')
                }
            }
        }
        catch (Exception e) {
            KeywordUtil.markWarning('Error occured while inspecting DOM Check :' + e.toString())

            classification = 'Error: DOMInspection'
        } 
    }
    
    // Step 3: Save Result
    // Extract Country Code
    String countryCode = URLUtils.extractCountryCode(url)

    // Add Results to Buffer
    ResultsBufferService.addResult(countryCode, url, finalUrl, classification)
}
catch (Exception e) {
    KeywordUtil.markWarning("Error occured while checking URL: $url" + e.getMessage())

    String countryCode = URLUtils.extractCountryCode(url)

    ResultsBufferService.addResult(countryCode, url, url, 'Error:Processing')
} 