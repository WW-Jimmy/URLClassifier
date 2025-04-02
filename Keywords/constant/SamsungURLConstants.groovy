package constant

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
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class SamsungURLConstants {
	// Out of Scope 경로 식별자
	public static final Set<String> OOS_PATHS = [
		'about-us',
		'account',
		'apps',
		'article',
		'buy',
		'buying-guide',
		'info',
		'mypage',
		'news',
		'service',
		'support',
		'sustainability'
	] as Set

	// 제품 카테고리 리스트
	public static final Set<String> PRODUCT_CATEGORIES = [
		'air-conditioners',
		'air-care',
		'audio-devices',
		'cooking-appliances',
		'dishwashers',
		'lifestyle-tvs',
		'laundry',
		'microwave-ovens',
		'monitors',
		'projectors',
		'refrigerators',
		'tvs',
		'vacuum-cleaners',
		'washers-and-dryers',
		'tablets'
	] as Set
}
