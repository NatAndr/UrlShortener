package com.andrianova.url.shortener.service;

import com.andrianova.url.shortener.model.Account;
import com.andrianova.url.shortener.model.AccountResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by natal on 29-May-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testAddAccountSuccessResult() {
        String login = "1";
        AccountResponse accountResponse = accountService.addAccount(login);
        assertTrue(accountResponse.isSuccess());
        assertEquals("Your account is opened", accountResponse.getDescription());
        assertNotNull(accountResponse.getPassword());
        assertEquals(8, accountResponse.getPassword().length());
    }

    @Test
    public void testAddAccountUnSuccessResult() {
        String login = "1";
        accountService.addAccount(login);
        AccountResponse accountResponse = accountService.addAccount(login);
        assertFalse(accountResponse.isSuccess());
        assertEquals("Account with that ID already exists", accountResponse.getDescription());
    }

    @Test
    public void testGetAccountByLogin() {
        String login = "1";
        accountService.addAccount(login);
        AccountResponse accountResponse = accountService.addAccount(login);
        Account accountByLogin = accountService.getAccountByLogin(login);
        assertNotNull(accountByLogin);
    }

    @Test
    public void testGetAccountById() {
        String login = "1";
        accountService.addAccount(login);
        AccountResponse accountResponse = accountService.addAccount(login);
        Account accountById = accountService.getAccountById(1);
        assertNotNull(accountById);
    }

    @Test
    public void testGetAccountsEmptyList() {
        List<Account> accounts = accountService.getAccounts();
        assertTrue(accounts.isEmpty());
    }

    @Test
    public void testGetAccountsNotEmptyList() {
        accountService.addAccount("1");
        accountService.addAccount("2");
        List<Account> accounts = accountService.getAccounts();
        assertEquals(2, accounts.size());
    }
}
