package com.wikia.webdriver.testcases.adstests;

import com.wikia.webdriver.common.core.url.Page;
import com.wikia.webdriver.common.dataprovider.ads.AdsDataProvider;
import com.wikia.webdriver.common.dataprovider.mobile.MobileAdsDataProvider;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.common.templates.TemplateNoFirstLoad;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.AdsBaseObject;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Dimension;
import org.testng.annotations.Test;

import java.util.Map;

public class TestAdsSlotSizes extends TemplateNoFirstLoad {

  private static Dimension MOBILE_SIZE = new Dimension(414, 736);
  private static Dimension DESKTOP_SIZE = new Dimension(1920, 1080);

  @Test(
      dataProviderClass = AdsDataProvider.class,
      dataProvider = "adsSlotSizeOasis",
      groups = "AdsSlotSizesOasis"
  )
  public void adsSlotSizesOasis(Page page,
                                String urlParamToEnable,
                                Map<String, Object> slotInfo) {

    adsSlotSizes(page, urlParamToEnable, DESKTOP_SIZE, slotInfo);
  }

  @Test(
      dataProviderClass = MobileAdsDataProvider.class,
      dataProvider = "adsSlotSizeMercury",
      groups = "AdsSlotSizesMercury"
  )
  public void adsSlotSizesMercury(Page page,
                                  String urlParamToEnable,
                                  Map<String, Object> slotInfo) {

    adsSlotSizes(page, urlParamToEnable, MOBILE_SIZE, slotInfo);
  }

  private void adsSlotSizes(Page page,
                            String urlParamToEnable,
                            Dimension pageSize,
                            Map<String, Object> slotInfo) {

    String slotName = slotInfo.get("slotName").toString();
    Dimension slotSize = (Dimension) slotInfo.get("slotSize");

    String url = urlBuilder.getUrlForPage(page);
    if (StringUtils.isNotEmpty(urlParamToEnable)) {
      url = urlBuilder.appendQueryStringToURL(url, urlParamToEnable);
    }

    log(slotName, slotSize);

    new AdsBaseObject(driver, url, pageSize)
        .triggerAdSlot(slotName)
        .verifyLineItemId(slotName, Integer.valueOf(slotInfo.get("lineItemId").toString()))
        .verifyIframeSize(slotName, slotInfo.get("src").toString(),
                          slotSize.getWidth(), slotSize.getHeight());
  }

  private void log(String slotName, Dimension slotSize) {
    PageObjectLogging.log("adsSlotSizes", "Slot name: " + slotName, true);
    PageObjectLogging.log("adsSlotSizes", "Width: " + slotSize.getWidth(), true);
    PageObjectLogging.log("adsSlotSizes", "Height: " + slotSize.getHeight(), true);
  }
}
