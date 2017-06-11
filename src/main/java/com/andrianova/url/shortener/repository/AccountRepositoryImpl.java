package com.andrianova.url.shortener.repository;

import com.andrianova.url.shortener.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by natal on 09-Jun-17.
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository<Account> {

    @Autowired
    protected EntityManager entityManager;

    public AccountRepositoryImpl() {
    }

    @Override
    public Account get(int id) {
        return entityManager.find(Account.class, id);
    }

    @Override
    public List<Account> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        CriteriaQuery<Account> select = criteriaQuery.select(criteriaQuery.from(Account.class));
        return entityManager.createQuery(select).getResultList();
    }

    @Override
    public void insert(final Account entity) throws DaoException {
        entityManager.persist(entity);
        entityManager.flush();
    }

    public Account getByLogin(String login) {
        Account account = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        Root<Account> accountRoot = criteriaQuery.from(Account.class);
        CriteriaQuery<Account> select = criteriaQuery.where(criteriaBuilder.equal(accountRoot.get("login"), login));
        try {
            account = entityManager.createQuery(select).getSingleResult();
        } catch (NoResultException e) {

        }
        return account;
    }
}
