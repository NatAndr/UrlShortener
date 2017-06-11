package com.andrianova.url.shortener.service;

import com.andrianova.url.shortener.model.AccountResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
//        String accountId = "1";
//        AccountResponse accountResponse = accountService.addAccount(accountId);
//        assertTrue(accountResponse.isSuccess());
//        assertEquals("Your account is opened", accountResponse.getDescription());
    }

    @Test
    public void testAddAccountUnSuccessResult() {
//        String accountId = "1";
//        accountService.addAccount(accountId);
//        AccountResponse accountResponse = accountService.addAccount(accountId);
//        assertFalse(accountResponse.isSuccess());
//        assertEquals("Account with that ID already exists", accountResponse.getDescription());
    }
}
