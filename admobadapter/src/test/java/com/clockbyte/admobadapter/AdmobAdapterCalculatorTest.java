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
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt0_Position0_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(0), is(0));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt0_Position1_Result0() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(1), is(10));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt0_Position10_Result100() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(10), is(100));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt0_Position11_Result110() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(11), is(110));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt5_Position0_Result5() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(0), is(5));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt5_Position1_Result15() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(1), is(15));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt5_Position10_Result105() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(10), is(105));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition_1stAdAt5_Position11_Result115() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 5, 10);
        assertThat(admobAdapterCalculator.translateAdToAdapterWrapperPosition(11), is(115));
    }

    @Test
    public void testCanShowAdAtPosition() throws Exception {

    }

    @Test
    public void testGetAdIndex() throws Exception {

    }

    @Test
    public void testIsAdPosition() throws Exception {

    }

    @Test
    public void testGetOffsetValue() throws Exception {

    }

    @Test
    public void testIsAdAvailable() throws Exception {

    }

    @Test
    public void testHasToFetchAd() throws Exception {

    }
}