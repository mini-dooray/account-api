package com.minidooray.accountapi.repository;

import com.minidooray.accountapi.entity.Account;

public interface AccountRepositoryCustom {

    Account getAccountByEmail(String email);

    Long countEmail(String email);

    Long countId(String id);

    Account getAccountById(String id);

    Account getAccountByIdAndPassword(String id, String password);
}
