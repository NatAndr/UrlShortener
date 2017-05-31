package com.example.url.shortener.service;

import com.example.url.shortener.Utils;
import com.example.url.shortener.model.Account;
import com.example.url.shortener.model.AccountResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by natal on 24-May-17.
 */
@Service
public class AccountService {
    private static final Logger LOG = Logger.getLogger(AccountService.class.getName());

    private static final int PASSWORD_LENGTH = 8;
    private static final String SUCCESS_DESCRIPTION = "Your account is opened";
    private static final String FAILED_DESCRIPTION = "Account with that ID already exists";

//    id: Account
    private volatile Map<String, Account> accounts = new ConcurrentHashMap<>();

    @PostConstruct
    public void test() {
        LOG.info("TEST @PostConstruct");
    }

    public AccountResponse addAccount(String id) {
        LOG.debug(String.format("Going to add account, ID [%s]", id));
        AccountResponse accountResponse = new AccountResponse();
        Account account = getAccountById(id);
        if (account != null) {
            String password = account.getPassword();
            accountResponse.setSuccess(false);
            accountResponse.setDescription(FAILED_DESCRIPTION);
            accountResponse.setPassword(password);
            LOG.info(String.format("Account with ID [%s] already exists", account.getId()));
        } else {
            account = new Account();
            account.setId(id);
//            String password = Utils.randomString(PASSWORD_LENGTH);
            String password = "123";
            account.setPassword(password);
            this.accounts.put(id, account);
            accountResponse.setSuccess(true);
            accountResponse.setDescription(SUCCESS_DESCRIPTION);
            accountResponse.setPassword(password);
            LOG.info(String.format("Account with ID [%s] is opened", account.getId()));
        }
        return accountResponse;
    }

    public Account getAccountById(String id) {
        return accounts.get(id);
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public Optional<Account> getAccountByLogin(String login) {
//        return accounts.values().stream()
//                .filter(line -> login.equals(line.getLogin()))
//                .findAny();
        return Optional.ofNullable(this.accounts.get(login));
    }

}
