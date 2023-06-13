package com.minidooray.accountapi.service;

import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import org.springframework.data.repository.NoRepositoryBean;



@NoRepositoryBean
public interface AccountService {

    ResponseAccountDto getAccount(Long seq);

    ResponseAccountDto deleteAccount(Long seq);

    ResponseAccountDto register(RequestAccountDto requestAccountDto);

    ResponseAccountDto updateAccount(Long seq,RequestAccountDto requestAccountDto);

    String getAccountId(Long seq);

    String getPassword(Long seq);

    String getEmail(Long seq);

    ResponseAccountDto getAccountByEmail(String email);

    boolean existEmail(String email);

    boolean existId(String id);

    boolean existLoginAccount(String id, String password);

    ResponseAccountDto getAccountById(String id);

    ResponseAccountDto updateAccessDate(Long seq);



}

