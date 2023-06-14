package com.minidooray.accountapi.repository.impl;

import com.minidooray.accountapi.entity.Account;
import com.minidooray.accountapi.entity.QAccount;
import com.minidooray.accountapi.entity.QAdditionalInfo;
import com.minidooray.accountapi.repository.AccountRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AccountRepositoryImpl extends QuerydslRepositorySupport implements AccountRepositoryCustom {


    private final EntityManager entityManager;
    public AccountRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        super(Account.class);
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Account getAccountByEmail(String email){
        JPAQuery<Account> query =new JPAQuery<>(entityManager);

        QAccount qAccount = QAccount.account;
        QAdditionalInfo qAdditionalInfo = QAdditionalInfo.additionalInfo;

        return query.select(qAccount)
                .from(qAccount)
                .join(qAccount.additionalInfo,qAdditionalInfo)
                .where(qAdditionalInfo.email.eq(email))
                .fetchOne();
    }
    @Override
    public Long countEmail(String email) {
        QAccount account = QAccount.account;
        QAdditionalInfo additionalInfo = QAdditionalInfo.additionalInfo;
        Long count;
        count = new JPAQuery<>(entityManager)
                .select(account.count())
                .from(account)
                .join(account.additionalInfo, additionalInfo)
                .where(additionalInfo.email.eq(email))
                .fetchOne();

        return count;
    }

    @Override
    public Long countId(String id) {
        QAccount qAccount = QAccount.account;

        Long count;
        count = new JPAQuery<>(entityManager)
                .select(qAccount.count())
                .from(qAccount)
                .where(qAccount.id.eq(id))
                .fetchOne();

        return count;
    }


    @Override
    public Account getAccountById(String id) {
        QAccount qAccount = QAccount.account;
        JPAQuery<Account> query = new JPAQuery<>(entityManager);

        return query
                .select(qAccount)
                .from(qAccount)
                .where(qAccount.id.eq(id))
                .fetchOne();
    }

    @Override
    public Account getAccountByIdAndPassword(String id, String password) {
        QAccount qAccount = QAccount.account;

        JPAQuery<Account> query = new JPAQuery<>(entityManager);
        return query
                .select(qAccount)
                .from(qAccount)
                .where(qAccount.id.eq(id).and(qAccount.password.eq(password)))
                .fetchOne();
    }
}
