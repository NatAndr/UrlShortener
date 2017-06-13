package com.andrianova.url.shortener.web;

import com.andrianova.url.shortener.model.Account;
import com.andrianova.url.shortener.model.AccountRequest;
import com.andrianova.url.shortener.model.AccountResponse;
import com.andrianova.url.shortener.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by natal on 12-Jun-17.
 */
@RestController
public class AccountController {
    private static final Logger LOG = Logger.getLogger(AccountController.class.getName());

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/account", method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<AccountResponse> addAccount(@RequestBody AccountRequest accountRequest) {
        Long start = System.currentTimeMillis();
        AccountResponse accountResponse = accountService.addAccount(accountRequest.getAccountId());
        LOG.info(String.format("Add account, elapsed [%s] ms", (System.currentTimeMillis() - start)));
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET,
            produces = "application/json")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }
}
