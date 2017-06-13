package com.andrianova.url.shortener.controller;

import com.andrianova.url.shortener.model.RegisteredUrl;
import com.andrianova.url.shortener.model.ShortUrlResponse;
import com.andrianova.url.shortener.service.UrlService;
import com.andrianova.url.shortener.web.UrlShortenerController;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by natal on 13-Jun-17.
 */
public class UrlShortenerControllerTest extends AbstractControllerTest {

//    @Autowired
//    private WebApplicationContext context;

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlShortenerController urlShortenerController;
    private String shortUrl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(urlShortenerController)
                .build();
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
    }

    @Ignore
    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void getMessageUnauthenticated() throws Exception {
//        urlShortenerController.getStatistic("1");
        mockMvc.perform(post("/statistic/1"));
    }

    @Ignore
    @Test
    public void testDoRegister() throws Exception {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse("http://localhost/384130bf");
        RegisteredUrl registeredUrl = new RegisteredUrl("http://www.baeldung.com/rest-api-spring-oauth2-angularjs",
                301);
        String accountId = "1";
        when(urlService.registerUrl(registeredUrl, accountId)).thenReturn(shortUrlResponse);
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(shortUrlResponse)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shortUrl", is(shortUrlResponse.getShortUrl())));
        verify(urlService, times(1)).registerUrl(registeredUrl, accountId);
    }

    @Test
    public void testRedirect() throws Exception {
        RegisteredUrl registeredUrl = new RegisteredUrl("http://www.baeldung.com/rest-api-spring-oauth2-angularjs",
                301);
        shortUrl = "384130bf";
        when(urlService.getUrl(shortUrl)).thenReturn(registeredUrl);
        mockMvc.perform(get("/{shortUrl}", shortUrl))
                .andExpect(status().is(registeredUrl.getRedirectType()))
                .andExpect(redirectedUrl(registeredUrl.getUrl()));
        verify(urlService, times(1)).getUrl(shortUrl);
    }

    @Test
    public void testRedirectDefaultType() throws Exception {
        RegisteredUrl registeredUrl = new RegisteredUrl("http://www.baeldung.com/rest-api-spring-oauth2-angularjs");
        shortUrl = "384130bf";
        when(urlService.getUrl(shortUrl)).thenReturn(registeredUrl);
        mockMvc.perform(get("/{shortUrl}", shortUrl))
                .andExpect(status().isFound());
        verify(urlService, times(1)).getUrl(shortUrl);
    }

    @Test
    public void testRedirectNotFound() throws Exception {
        shortUrl = "384130bf";
        when(urlService.getUrl(shortUrl)).thenReturn(null);
        mockMvc.perform(get("/{shortUrl}", shortUrl))
                .andExpect(status().isNotFound());
        verify(urlService, times(1)).getUrl(shortUrl);
    }
}
