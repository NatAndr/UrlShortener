package com.andrianova.url.shortener.web;

import com.andrianova.url.shortener.model.RegisteredUrl;
import com.andrianova.url.shortener.model.ShortUrlResponse;
import com.andrianova.url.shortener.service.UrlService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * Created by natal on 24-May-17.
 */
@RestController
public class UrlShortenerController {
    private static final Logger LOG = Logger.getLogger(UrlShortenerController.class.getName());
    private static final int DEFAULT_STATUS = HttpServletResponse.SC_FOUND;

    @Autowired
    private UrlService urlService;

    @RequestMapping(value = "/register", method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<ShortUrlResponse> doRegister(@RequestBody RegisteredUrl registeredUrl) {
        Long start = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        ShortUrlResponse shortUrlResponse = urlService.registerUrl(registeredUrl, loggedUsername);
        LOG.info(String.format("Registered %s, loggedUsername [%s]", registeredUrl, loggedUsername));
        LOG.info(String.format("Register url, elapsed [%s] ms", (System.currentTimeMillis() - start)));
        return new ResponseEntity<>(shortUrlResponse, HttpStatus.OK);
    }

    @RequestMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse response) throws Exception {
        final RegisteredUrl registeredUrl = urlService.getUrl(shortUrl);
        if (registeredUrl != null) {
            LOG.info(String.format("Redirect: short URL [%s] to [%s]", shortUrl, registeredUrl.getUrl()));
            response.addHeader("Location", registeredUrl.getUrl());
            final int redirectType = registeredUrl.getRedirectType() != null ?
                    registeredUrl.getRedirectType() : DEFAULT_STATUS;
            response.setStatus(redirectType);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/statistic/{accountId}", method = RequestMethod.GET,
            headers = "Accept=application/json",
            produces = {"application/json"})
    public ResponseEntity getStatistic(@PathVariable String accountId) {
        Long start = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (accountId.equals(loggedUsername)) {
            Object statistic = urlService.getStatistic(accountId);
            LOG.info(String.format("Get statistic, elapsed [%s] ms", (System.currentTimeMillis() - start)));
            return new ResponseEntity<>(statistic, HttpStatus.OK);
        } else {
            LOG.error(String.format("AccountId [%s], loggedUsername [%s]", accountId, loggedUsername));
            throw new RuntimeException("Provided accountId doesn't match to request credentials");
        }
    }
}
