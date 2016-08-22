package com.clockbyte.admobadapter;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * Created by FILM on 21.08.2016.
 */
public class AdmobAdapterCalculatorTest {

    AdmobAdapterCalculator admobAdapterCalculator;
    @Before
    public void setUp() throws Exception {
        admobAdapterCalculator = new AdmobAdapterCalculator();
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
    public void testGetAdsCountToPublish_itemsBetweenAds0_Result100() throws Exception {
        assertThat(testGetAdsCountToPublishHelper(1, 0, 200, 200, 100), is(101));
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
    public void testGetOriginalContentPosition() throws Exception {
        setupAdmobAdapterCalculatorHelper(10, 0, 10);
        assertThat(admobAdapterCalculator.getOriginalContentPosition(0, 10, 100), is(0));
    }

    @Test
    public void testTranslateAdToAdapterWrapperPosition() throws Exception {

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