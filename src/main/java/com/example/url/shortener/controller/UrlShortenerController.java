package com.example.url.shortener.controller;

import com.example.url.shortener.model.AccountResponse;
import com.example.url.shortener.model.RegisteredUrl;
import com.example.url.shortener.model.ShortUrlResponse;
import com.example.url.shortener.service.AccountService;
import com.example.url.shortener.service.OauthService;
import com.example.url.shortener.service.UrlService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;


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

    @Autowired
    private OauthService oauthService;

//    @Resource(name="tokenStore")
//    private TokenStore tokenStore;

//    @Autowired
//    private DefaultTokenServices tokenServices;
//    @Autowired
//    private TokenEndpoint tokenEndpoint;
//
//    @Autowired
//    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/account", method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity addAccount(@RequestBody String accountId) {
        Long start = System.currentTimeMillis();
        AccountResponse accountResponse = accountService.addAccount(accountId);
        LOG.info(String.format("Add account, elapsed [%s] ms", (System.currentTimeMillis() - start)));
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
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
//        String statistic = urlService.getStatistic(accountId);
        Object statistic = urlService.getStatistic(accountId);
        LOG.info(String.format("Get statistic, elapsed [%s] ms", (System.currentTimeMillis() - start)));
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Object read(OAuth2Authentication auth) {
        //auth.getAuthorizationRequest().getClientId()
        return auth.getCredentials();
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public Object read2(OAuth2Authentication auth, @RequestHeader(value="Authorization") String authorizationHeader,
                        Principal currentUser) {
        //auth.getAuthorizationRequest().getClientId()
//        Object credentials = auth.getCredentials();
//        Object principal = auth.getPrincipal();
        String token2 = oauthService.getToken2();
        return token2;
    }


    public int test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        Set<String> nameSet = new TreeSet<>(Arrays.asList("Mr.Green", "Mr.Yellow", "Mr.Red"));
        nameSet.forEach(System.out::println);

        Map<String, Integer> map = new TreeMap<>();
        map.put("Gamma", 3);
        map.put("Omega", 24);
        map.put("Alpha", 1);

        for (Map.Entry<String, Integer> m : map.entrySet()) {
            System.out.println(m.getKey() + "=" + m.getValue());
        }
        Scanner in = new Scanner(System.in);
        int v = in.nextInt();
        int t = in.nextInt();
        return Math.abs(Math.abs(v) * t - 109);
    }
}
