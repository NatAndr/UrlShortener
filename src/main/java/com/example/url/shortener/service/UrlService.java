package com.example.url.shortener.service;

import com.example.url.shortener.model.RegisteredUrl;
import com.example.url.shortener.model.ShortUrlResponse;
import com.google.common.hash.Hashing;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by natal on 24-May-17.
 */
@Service
public class UrlService {
    private static final Logger LOG = Logger.getLogger(UrlService.class.getName());

    @Value("${host.name}")
    private String hostName;

    //    urlHash : url
    private Map<String, RegisteredUrl> urls = new ConcurrentHashMap<>();

    //  accountId : urls list
    private Map<String, List<String>> accountIdToUrlMap = new ConcurrentHashMap<>();

    //    url : count
    private Map<String, Integer> statistics = new ConcurrentHashMap<>();

    public ShortUrlResponse registerUrl(RegisteredUrl registeredUrl, String accountId) {
        LOG.info("registerUrl");
        final String url = registeredUrl.getUrl();
        final String urlHash = getHash(url);
        urls.put(urlHash, registeredUrl);
        if (accountIdToUrlMap.containsKey(accountId)) {
            List<String> urlList = accountIdToUrlMap.get(accountId);
            urlList.add(url);
            accountIdToUrlMap.put(accountId, urlList);
        } else {
            accountIdToUrlMap.put(accountId, new ArrayList<>(Arrays.asList(url)));
        }
        return new ShortUrlResponse(hostName + "/" + urlHash);
    }

    public RegisteredUrl getUrl(String shortUrl) {
        LOG.info("getUrl: " + shortUrl);
        final RegisteredUrl registeredUrl = urls.get(shortUrl);
        if (registeredUrl != null) {
            final String url = registeredUrl.getUrl();
            AtomicInteger count = new AtomicInteger(0);
            if (statistics.containsKey(url)) {
                count.set(statistics.get(url));
            }
            statistics.put(url, count.incrementAndGet());
        }
        return registeredUrl;
    }

    public Object getStatistic(String accountId) {
        LOG.info("getStatistic");
        Map<String, Integer> result = new ConcurrentHashMap<>();
        if (accountIdToUrlMap.isEmpty()) {
            return result;
        }
        List<String> urlList = accountIdToUrlMap.get(accountId);
        if (!urlList.isEmpty() && !statistics.isEmpty()) {
            for (String url : urlList) {
                result.put(url, statistics.get(url));
            }
        }
        return result;
    }

    private String getHash(String url) {
        return Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
    }

}
