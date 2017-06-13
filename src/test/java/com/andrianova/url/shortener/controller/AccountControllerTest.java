package com.andrianova.url.shortener.controller;

import com.andrianova.url.shortener.model.Account;
import com.andrianova.url.shortener.model.AccountRequest;
import com.andrianova.url.shortener.model.AccountResponse;
import com.andrianova.url.shortener.service.AccountService;
import com.andrianova.url.shortener.web.AccountController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by natal on 12-Jun-17.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//@WebAppConfiguration
public class AccountControllerTest extends AbstractControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .build();
    }

    @Test
    public void testGetAccounts() throws Exception {
        List<Account> accounts = Arrays.asList(
                new Account(1, "123", "1"),
                new Account(2, "456", "2"));
        when(accountService.getAccounts()).thenReturn(accounts);
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].password", is("123")))
                .andExpect(jsonPath("$[0].login", is("1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].password", is("456")))
                .andExpect(jsonPath("$[1].login", is("2")));
        verify(accountService, times(1)).getAccounts();
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void testAddAccountSuccess() throws Exception {
        AccountResponse accountResponse = new AccountResponse(true, "Your account is opened", "123");
        AccountRequest accountRequest = new AccountRequest("1");
        when(accountService.addAccount("1")).thenReturn(accountResponse);
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.description", is("Your account is opened")))
                .andExpect(jsonPath("$.password", is("123")));
        verify(accountService, times(1)).addAccount("1");
    }

    @Test
    public void testAddAccountUnSuccess() throws Exception {
        AccountResponse accountResponse = new AccountResponse(false, "Account with that ID already exists", "123");
        AccountRequest accountRequest = new AccountRequest("1");
        when(accountService.addAccount("1")).thenReturn(accountResponse);
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.description", is("Account with that ID already exists")))
                .andExpect(jsonPath("$.password", is("123")));
        verify(accountService, times(1)).addAccount("1");
    }
}
