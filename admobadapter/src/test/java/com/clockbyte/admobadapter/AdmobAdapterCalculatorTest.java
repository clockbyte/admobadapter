package com.clockbyte.admobadapter;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by FILM on 21.08.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class AdmobAdapterCalculatorTest {

    AdmobAdapterCalculator admobAdapterCalculator;
    @Before
    public void setUp() throws Exception {
        admobAdapterCalculator = new AdmobAdapterCalculator();
    }

    @Before
    public void prepareMocks(){
        mockStatic(Log.class);

        when(Log.d(any(String.class), any(String.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 0;
            }
        });
    }

    @Test
    public void testGetAdsCountToPublish_fetchedCntIs3_Result3() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 0, 10, 3, 100), is(3));
    }

    @Test
    public void testGetAdsCountToPublish_fetchedCntIs0_Result0() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 0, 10, 0, 100), is(0));
    }

    @Test
    public void testGetAdsCountToPublish_sourceItemsCntIs1_Result1() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 0, 10, 3, 1), is(1));
    }

    @Test
    public void testGetAdsCountToPublish_sourceItemsCntIs0_Result0() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 0, 10, 10, 0), is(0));
    }

    @Test
    public void testGetAdsCountToPublish_firstIdxIs99_Result1() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 99, 10, 10, 100), is(1));
    }

    @Test
    public void testGetAdsCountToPublish_firstIdxIs10_Result10() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 10, 10, 10, 100), is(10));
    }

    @Test
    public void testGetAdsCountToPublish_firstIdxIs11_Result9() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 11, 10, 10, 100), is(9));
    }

    @Test
    public void testGetAdsCountToPublish_firstIdxIs0_Result10() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 0, 10, 10, 100), is(10));
    }

    @Test
    public void testGetAdsCountToPublish_itemsBetweenAds1_Result100() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(1, 0, 200, 200, 100), is(101));
    }

    @Test
    public void testGetAdsCountToPublish_itemsBetweenAds0_Result0() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(0, 0, 200, 200, 100), is(0));
    }

    @Test
    public void testGetAdsCountToPublish_limitAdsIs0_Result0() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 0, 0, 10, 100), is(0));
    }

    @Test
    public void testGetAdsCountToPublish_limitAdsIs10_Result10() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(10, 0, 10, 100, 100), is(10));
    }

    private int testGetAdsCountToPublishHelper(int itemsBetweenAds, int firstAdIndex, int limitAds,
                                               int fetchedAdsCnt, int sourceItemsCnt){
        setupAdmobAdapterCalculatorHelper(itemsBetweenAds, firstAdIndex, limitAds);
        return admobAdapterCalculator.getAdsCountToPublish(fetchedAdsCnt, sourceItemsCnt);
    }

    private void setupAdmobAdapterCalculatorHelper(int itemsBetweenAds, int firstAdIndex, int limitAds){
        admobAdapterCalculator.setNoOfDataBetweenAds(itemsBetweenAds);
        admobAdapterCalculator.setFirstAdIndex(firstAdIndex);
        admobAdapterCalculator.setLimitOfAds(limitAds);
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt0_Position0_Result_1() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(0, 10, 100), is(-1));
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt0_Position1_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(1, 10, 100), is(0));
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt0_Position10_Result9() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(10, 10, 100), is(9));
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt0_Position11_Result9() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(11, 10, 100), is(9));
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt0_Position12_Result10() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(12, 10, 100), is(10));
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt0_Position110_Result100() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(110, 10, 100), is(100));
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt5_Position4_Result4() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(4, 10, 100), is(4));
    }

    @Test
    public void testGetOriginalContentPosition_1stAdAt5_Position10_Result9() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(10, 10, 100), is(9));
    }


    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt0_Position0_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(0), is(0));
    }

    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt0_Position1_Result11() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(1), is(11));
    }

    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt0_Position10_Result110() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(10), is(110));
    }

    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt0_Position11_Result121() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(11), is(121));
    }

    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt5_Position0_Result5() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(0), is(5));
    }

    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt5_Position1_Result16() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(1), is(16));
    }

    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt5_Position10_Result115() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(10), is(115));
    }

    @Test
    public void testTranslateAdToWrapperPosition_1stAdAt5_Position11_Result126() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToWrapperPosition(11), is(126));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_Position0_Fetched3_Result1() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(0, 3), is(1));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_Position41_Fetched3_Result44() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(41,3), is(44));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_Position41_Fetched10_Result46() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(41,10), is(46));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_Position90_Fetched10_Result100() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(90,10), is(100));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_Position89_Fetched10_Result98() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(89,10), is(98));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_1stAdAt5_Position0_Fetched3_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(0,3), is(0));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_1stAdAt5_Position44_Fetched10_Result48() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(44, 10), is(48));
    }

    @Test
    public void testTranslateSourceIndexToWrapperPosition_1stAdAt5_Position45_Fetched10_Result50() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateSourceIndexToWrapperPosition(45, 10), is(50));
    }

    @Test
    public void testCanShowAdAtPosition0_fetchedCntIs3_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(0, 3), is(true));
    }

    @Test
    public void testCanShowAdAtPosition22_fetchedCntIs3_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(22, 3), is(true));
    }

    @Test
    public void testCanShowAdAtPosition33_fetchedCntIs3_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(33, 3), is(false));
    }

    @Test
    public void testCanShowAdAtPosition0_fetchedCntIs0_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(0, 0), is(false));
    }

    @Test
    public void testCanShowAdAtPosition99_firstIdxIs99_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(99, 10), is(true));
    }

    @Test
    public void testCanShowAdAtPosition98_firstIdxIs99_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(98, 10), is(false));
    }

    @Test
    public void testCanShowAdAtPosition100_firstIdxIs99_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(100, 10), is(false));
    }

    @Test
    public void testCanShowAdAtPosition10_firstIdxIs10_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 10, 10);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(10, 10), is(true));
    }

    @Test
    public void testCanShowAdAtPosition0_itemsBetweenAds0_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(0, 0, 200);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(0, 200), is(true));
    }

    @Test
    public void testCanShowAdAtPosition0_limitAdsIs0_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 0);
        assertThat(admobAdapterCalculator.canShowAdAtPosition(0, 100), is(false));
    }

    private final int Fail = -1;

    @Test
    public void testGetAdIndex_position0_firstIdxIs0_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getAdIndex(0), is(0));
    }

    @Test
    public void testGetAdIndex_position99_firstIdxIs99_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.getAdIndex(99), is(0));
    }

    @Test
    public void testGetAdIndex_position98_firstIdxIs99_ResultFail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.getAdIndex(98), is(Fail));
    }

    @Test
    public void testGetAdIndex_position100_firstIdxIs99_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.getAdIndex(100), is(0));
    }

    @Test
    public void testGetAdIndex_position10_firstIdxIs10_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 10, 10);
        assertThat(admobAdapterCalculator.getAdIndex(10), is(0));
    }

    @Test
    public void testGetAdIndex_position0_itemsBetweenAds0_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(0, 0, 200);
        assertThat(admobAdapterCalculator.getAdIndex(0), is(0));
    }

    @Test
    public void testGetAdIndex_position0_limitAdsIs0_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 0);
        assertThat(admobAdapterCalculator.getAdIndex(0), is(0));
    }

    @Test
    public void testIsAdAvailable_position0_fetchedCntIs3_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(0, 3), is(true));
    }

    @Test
    public void testIsAdAvailable_position22_fetchedCntIs3_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(22, 3), is(true));
    }

    @Test
    public void testIsAdAvailable_position33_fetchedCntIs3_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(33, 3), is(false));
    }

    @Test
    public void testIsAdAvailable_fetchedCntIs0_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(0, 0), is(false));
    }

    @Test
    public void testIsAdAvailable_position99_firstIdxIs99_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(99, 10), is(true));
    }

    @Test
    public void testIsAdAvailable_position98_firstIdxIs99_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(98, 10), is(false));
    }

    @Test
    public void testIsAdAvailable_position100_firstIdxIs99_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(100, 10), is(true));
    }

    @Test
    public void testIsAdAvailable_position10_firstIdxIs10_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 10, 10);
        assertThat(admobAdapterCalculator.isAdAvailable(10, 10), is(true));
    }

    @Test
    public void testIsAdAvailable_position0_itemsBetweenAds0_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(0, 0, 200);
        assertThat(admobAdapterCalculator.isAdAvailable(0, 200), is(true));
    }

    @Test
    public void testIsAdAvailable_position0_limitAdsIs0_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 0);
        assertThat(admobAdapterCalculator.isAdAvailable(0, 100), is(false));
    }

    @Test
    public void testHasToFetchAd_position0_fetchedCntIs3_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(0, 3), is(false));
    }

    @Test
    public void testHasToFetchAd_position22_fetchedCntIs3_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(22, 3), is(false));
    }

    @Test
    public void testHasToFetchAd_position33_fetchedCntIs3_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(33, 3), is(true));
    }

    @Test
    public void testHasToFetchAd_fetchedCntIs0_Success() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(0, 0), is(true));
    }

    @Test
    public void testHasToFetchAd_position99_firstIdxIs99_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(99, 10), is(false));
    }

    @Test
    public void testHasToFetchAd_position98_firstIdxIs99_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(98, 10), is(false));
    }

    @Test
    public void testHasToFetchAd_position100_firstIdxIs99_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 99, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(100, 10), is(false));
    }

    @Test
    public void testHasToFetchAd_position10_firstIdxIs10_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 10, 10);
        assertThat(admobAdapterCalculator.hasToFetchAd(10, 10), is(false));
    }

    @Test
    public void testHasToFetchAd_position0_itemsBetweenAds0_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(0, 0, 200);
        assertThat(admobAdapterCalculator.hasToFetchAd(0, 200), is(false));
    }

    @Test
    public void testHasToFetchAd_position0_limitAdsIs0_Fail() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 0);
        assertThat(admobAdapterCalculator.hasToFetchAd(0, 100), is(false));
    }
}