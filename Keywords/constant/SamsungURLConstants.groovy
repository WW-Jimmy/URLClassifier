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
	public static final Set<String> OOS_PATHS = [
		'about-us',
		'account',
		'apps',
		'article',
		'business',
		'edm',
		'shop-faq',
		'info',
		'mypage',
		'news',
		'service',
		'support',
		'sustainability',
		'multistore',
	] as Set

	public static final Set<String> MKTPD_PATHS = [
		'smartphones/galaxy-z-flip6',
		'smartphones/galaxy-z-fold6',
		'smartphones/galaxy-s25-ultra',
		'smartphones/galaxy-s25',
		'smartphones/galaxy-s24',
		'smartphones/galaxy-s24-ultra',
		'smartphones/galaxy-s24-ultra/compare',
		'smartphones/galaxy-s24-ultra/upgrade',
		'smartphones/galaxy-s24-ultra/reviews',
		'smartphones/galaxy-s24/compare',
		'smartphones/galaxy-s24/upgrade',
		'smartphones/galaxy-s24/reviews',
		'smartphones/galaxy-s25/compare',
		'smartphones/galaxy-s25/reviews',
		'smartphones/galaxy-s25-ultra/compare',
		'smartphones/galaxy-s25-ultra/reviews',
		'smartphones/galaxy-z-flip6/compare',
		'smartphones/galaxy-z-flip6/reviews',
		'smartphones/galaxy-z-flip6/upgrade',
		'smartphones/galaxy-z-fold6/compare',
		'smartphones/galaxy-z-fold6/reviews',
		'smartphones/galaxy-z-fold6/upgrade',
		'mobile/switch-to-galaxy'
	] as Set

	/*
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
	 */
}
