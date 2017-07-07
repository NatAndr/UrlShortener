package com.andrianova.url.shortener.service;

import com.andrianova.url.shortener.model.RegisteredUrl;
import com.andrianova.url.shortener.model.ShortUrlResponse;
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
import java.util.concurrent.ConcurrentMap;
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
    private final ConcurrentMap<String, RegisteredUrl> urls = new ConcurrentHashMap<>();

    //  accountId : urls list
    private final Map<String, List<String>> accountIdToUrlMap = new ConcurrentHashMap<>();

    //    url : count
    private final Map<String, Integer> statistics = new ConcurrentHashMap<>();

    public Map<String, RegisteredUrl> getUrls() {
        return urls;
    }

    public Map<String, List<String>> getAccountIdToUrlMap() {
        return accountIdToUrlMap;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    /**
     * Register url. Add url to map: key = accountId, value = List<url>
     *
     * @param registeredUrl url to be registered
     * @param accountId
     * @return ShortUrlResponse
     */
    public ShortUrlResponse registerUrl(RegisteredUrl registeredUrl, String accountId) {
        LOG.debug("registerUrl");
        final String url = registeredUrl.getUrl();
        final String urlHash = getHash(url);
        urls.putIfAbsent(urlHash, registeredUrl);
        if (accountIdToUrlMap.containsKey(accountId)) {
            List<String> urlList = accountIdToUrlMap.get(accountId);
            urlList.add(url);
            accountIdToUrlMap.put(accountId, urlList);
        } else {
            accountIdToUrlMap.put(accountId, new ArrayList<>(Arrays.asList(url)));
        }
        return new ShortUrlResponse(hostName + "/" + urlHash);
    }

    /**
     * Get real url by url hash
     *
     * @param shortUrl = url hash
     * @return RegisteredUrl
     */
    public RegisteredUrl getUrl(String shortUrl) {
        LOG.debug("getUrl: " + shortUrl);
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

    /**
     * Get statistic by accountId. It contains urls and redirected number
     *
     * @param accountId
     * @return Map<url, count>
     */
    public Map<String, Integer> getStatistic(String accountId) {
        LOG.debug("getStatistic");
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

    /**
     * Get short url by real one
     *
     * @param url to be hashed
     * @return string hash
     */
    private String getHash(String url) {
        return Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
    }

}
