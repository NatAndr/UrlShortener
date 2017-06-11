package com.andrianova.url.shortener.web;

import com.andrianova.url.shortener.model.*;
import com.andrianova.url.shortener.service.AccountService;
import com.andrianova.url.shortener.service.UrlService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by natal on 24-May-17.
 */
@RestController
public class UrlShortenerController {
    private static final Logger LOG = Logger.getLogger(UrlShortenerController.class.getName());
    private static final int DEFAULT_STATUS = HttpServletResponse.SC_FOUND;

    @Autowired
    private AccountService accountService;
    @Autowired
    private UrlService urlService;

    @RequestMapping(value = "/account", method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity addAccount(@RequestBody AccountRequest accountRequest) {
        Long start = System.currentTimeMillis();
        AccountResponse accountResponse = accountService.addAccount(accountRequest.getAccountId());
        LOG.info(String.format("Add account, elapsed [%s] ms", (System.currentTimeMillis() - start)));
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET,
            produces = "application/json",
            consumes = "application/json")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity doRegister(@RequestBody RegisteredUrl registeredUrl) {
        Long start = System.currentTimeMillis();
        ShortUrlResponse shortUrlResponse = urlService.registerUrl(registeredUrl, "1");
        LOG.info(String.format("Register url, elapsed [%s] ms", (System.currentTimeMillis() - start)));
        return new ResponseEntity<>(shortUrlResponse, HttpStatus.OK);
    }

    @RequestMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse response) throws Exception {
        final RegisteredUrl registeredUrl = urlService.getUrl(shortUrl);
        if (registeredUrl != null) {
            response.addHeader("Location", registeredUrl.getUrl());
            final int redirectType = registeredUrl.getRedirectType() != null ?
                    Integer.parseInt(registeredUrl.getRedirectType()) : DEFAULT_STATUS;
            response.setStatus(redirectType);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/statistic/{accountId}", method = RequestMethod.GET, headers = "Accept=application/json",
            produces = {"application/json"})
    public ResponseEntity getStatistic(@PathVariable String accountId) {
        Long start = System.currentTimeMillis();
        Object statistic = urlService.getStatistic(accountId);
        LOG.info(String.format("Get statistic, elapsed [%s] ms", (System.currentTimeMillis() - start)));
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }
}
