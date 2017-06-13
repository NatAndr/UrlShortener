package com.andrianova.url.shortener.service;

import com.andrianova.url.shortener.Utils;
import com.andrianova.url.shortener.model.Account;
import com.andrianova.url.shortener.model.AccountResponse;
import com.andrianova.url.shortener.repository.AccountRepository;
import com.andrianova.url.shortener.repository.DaoException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by natal on 24-May-17.
 */
@Service
public class AccountService {
    private static final Logger LOG = Logger.getLogger(AccountService.class.getName());

    private static final int PASSWORD_LENGTH = 8;
    private static final String SUCCESS_DESCRIPTION = "Your account is opened";
    private static final String FAILED_DESCRIPTION = "Account with that ID already exists";

    private final AccountRepository<Account> repository;

    @Autowired
    public AccountService(AccountRepository<Account> repository) {
        this.repository = repository;
    }

    @Transactional
    public AccountResponse addAccount(String login) {
        LOG.debug(String.format("Going to add account, ID [%s]", login));
        AccountResponse accountResponse = new AccountResponse();
        Account account = getAccountByLogin(login);
        if (account != null) {
            String password = account.getPassword();
            accountResponse.setSuccess(false);
            accountResponse.setDescription(FAILED_DESCRIPTION);
            accountResponse.setPassword(password);
            LOG.info(String.format("Account with ID [%s] already exists", account.getId()));
        } else {
            account = new Account();
            account.setLogin(login);
            String password = Utils.randomString(PASSWORD_LENGTH);
            account.setPassword(password);
            try {
                this.repository.insert(account);
            } catch (DaoException e) {
                LOG.error("Error: " + e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
            accountResponse.setSuccess(true);
            accountResponse.setDescription(SUCCESS_DESCRIPTION);
            accountResponse.setPassword(password);
            LOG.info(String.format("Account with ID [%s] is opened", account.getId()));
        }
        return accountResponse;
    }

    @Transactional(readOnly = true)
    public Account getAccountById(int id) {
        return this.repository.get(id);
    }

    @Transactional(readOnly = true)
    public List<Account> getAccounts() {
        return this.repository.getAll();
    }

    @Transactional(readOnly = true)
    public Account getAccountByLogin(String login) {
        return this.repository.getByLogin(login);
    }
}
