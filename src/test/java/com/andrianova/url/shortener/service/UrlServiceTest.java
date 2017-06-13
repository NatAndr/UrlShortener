package com.andrianova.url.shortener.service;

import com.andrianova.url.shortener.model.RegisteredUrl;
import com.andrianova.url.shortener.model.ShortUrlResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by natal on 29-May-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UrlServiceTest {

    @Autowired
    private UrlService urlService;

    @Test
    public void testRegisterUrl() {
        RegisteredUrl registeredUrl = new RegisteredUrl("http://www.baeldung.com/rest-api-spring-oauth2-angularjs",
                301);
        String accountId = "1";
        ShortUrlResponse shortUrlResponse = urlService.registerUrl(registeredUrl, accountId);
        assertNotNull(shortUrlResponse);
        List<String> urls = urlService.getAccountIdToUrlMap().get(accountId);
        assertEquals(1, urls.size());
    }

    @Test
    public void testGetStatisticEmpty() {
        String accountId = "1";
        Map<String, Integer> statistic = urlService.getStatistic(accountId);
        assertTrue(statistic.isEmpty());
    }

    @Test
    public void testGetStatistic() {
        String accountId = "1";

        //first url
        RegisteredUrl registeredUrl = new RegisteredUrl("http://www.baeldung.com/rest-api-spring-oauth2-angularjs",
                301);
        ShortUrlResponse shortUrlResponse = urlService.registerUrl(registeredUrl, accountId);
        String shortUrl = shortUrlResponse.getShortUrl();
        String urlHash = getHashSubstringFromUrl(shortUrl);
        //get url for redirect two times
        urlService.getUrl(urlHash);
        urlService.getUrl(urlHash);

        //second url
        RegisteredUrl registeredUrl2 = new RegisteredUrl("http://www.google.com", 301);
        ShortUrlResponse shortUrlResponse2 = urlService.registerUrl(registeredUrl2, accountId);
        String shortUrl2 = shortUrlResponse2.getShortUrl();
        String urlHash2 = getHashSubstringFromUrl(shortUrl2);
        //get url for redirect tree times
        urlService.getUrl(urlHash2);
        urlService.getUrl(urlHash2);
        urlService.getUrl(urlHash2);

        Map<String, Integer> actualStatistic = urlService.getStatistic(accountId);
        Map<String, Integer> expectedStatistic = new HashMap<>();
        expectedStatistic.put("http://www.baeldung.com/rest-api-spring-oauth2-angularjs", 2);
        expectedStatistic.put("http://www.google.com", 3);
        assertEquals(expectedStatistic, actualStatistic);
    }

    @Test
    public void testGetUrl() {
        RegisteredUrl registeredUrl = new RegisteredUrl("http://www.baeldung.com/rest-api-spring-oauth2-angularjs",
                301);
        String accountId = "1";
        ShortUrlResponse shortUrlResponse = urlService.registerUrl(registeredUrl, accountId);
        String shortUrl = shortUrlResponse.getShortUrl();
        String urlHash = getHashSubstringFromUrl(shortUrl);

        RegisteredUrl gotUrl = urlService.getUrl(urlHash);
        assertEquals(registeredUrl.getUrl(), gotUrl.getUrl());
    }

    @Test
    public void testGetUrlIsNull() {
        RegisteredUrl registeredUrl = new RegisteredUrl("http://www.baeldung.com/rest-api-spring-oauth2-angularjs",
                301);
        String accountId = "1";
        ShortUrlResponse shortUrlResponse = urlService.registerUrl(registeredUrl, accountId);
        String wrongUrlHash = "test";

        RegisteredUrl gotUrl = urlService.getUrl(wrongUrlHash);
        assertNull(gotUrl);
    }

    private String getHashSubstringFromUrl(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }
}
