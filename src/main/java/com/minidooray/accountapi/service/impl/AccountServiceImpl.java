package com.minidooray.accountapi.service.impl;

import com.minidooray.accountapi.entity.*;
import com.minidooray.accountapi.repository.AccountRepository;
import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import com.minidooray.accountapi.service.AccountService;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl extends QuerydslRepositorySupport implements AccountService {


    private final AccountRepository accountRepository;
    private final EntityManager entityManager;


    private final QAccount account = QAccount.account;
    private final QAdditionalInfo additionalInfo = QAdditionalInfo.additionalInfo;

    public AccountServiceImpl(AccountRepository accountRepository, EntityManager entityManager) {
        super(Account.class);
        this.accountRepository = accountRepository;
        this.entityManager = entityManager;
    }




    @Override
    public ResponseAccountDto getAccount(Long seq) {
        Account account = accountRepository.findById(seq)
                .orElse(null);



        return ResponseAccountDto.builder()
                .accountSeq(account.getSeq())
                .accountId(account.getId())
                .password(account.getPassword())
                .name(account.getName())
                .email(account.getAdditionalInfo().getEmail())
                .phoneNumber(account.getAdditionalInfo().getPhoneNumber())
                .status(account.getAccountStatus().getStatus())
                .lastAccessDate(account.getAccountStatus().getAccessDate())
                .build();

    }

    @Override
    public ResponseAccountDto deleteAccount(Long seq) {
        Account account = accountRepository.findById(seq)
                .orElse(null);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        accountRepository.deleteById(seq);

        return ResponseAccountDto.builder()
                .accountSeq(account.getSeq())
                .accountId(account.getId())
                .password(account.getPassword())
                .name(account.getName())
                .email(account.getAdditionalInfo().getEmail())
                .phoneNumber(account.getAdditionalInfo().getPhoneNumber())
                .status(account.getAccountStatus().getStatus())
                .lastAccessDate(account.getAccountStatus().getAccessDate())
                .build();
    }

    public ResponseAccountDto register(RequestAccountDto request) {

        Account account = new Account();
        account.setId(request.getAccountId());
        account.setPassword(request.getPassword());
        account.setName(request.getName());

        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setEmail(request.getEmail());
        additionalInfo.setPhoneNumber(request.getPhoneNumber());
        additionalInfo.setAccount(account);
        account.setAdditionalInfo(additionalInfo);

        AccountStatus accountStatus = new AccountStatus();
        accountStatus.setStatus(request.getStatus());
        accountStatus.setAccount(account);
        account.setAccountStatus(accountStatus);

        Account savedAccount = accountRepository.saveAndFlush(account);

        return ResponseAccountDto.builder()
                .accountSeq(savedAccount.getSeq())
                .accountId(savedAccount.getId())
                .password(savedAccount.getPassword())
                .name(savedAccount.getName())
                .email(savedAccount.getAdditionalInfo().getEmail())
                .phoneNumber(savedAccount.getAdditionalInfo().getPhoneNumber())
                .status(savedAccount.getAccountStatus().getStatus())
                .lastAccessDate(savedAccount.getAccountStatus().getAccessDate())
                .build();

    }


    public ResponseAccountDto updateAccount(Long seq,RequestAccountDto requestAccountDto) {
        Account account = accountRepository.findById(seq)
                .orElse(null);
        account.setPassword(requestAccountDto.getPassword());
        account.setName(requestAccountDto.getName());

        accountRepository.saveAndFlush(account);

        return ResponseAccountDto.builder()
                .accountSeq(account.getSeq())
                .accountId(account.getId())
                .password(account.getPassword())
                .name(account.getName())
                .email(account.getAdditionalInfo().getEmail())
                .phoneNumber(account.getAdditionalInfo().getPhoneNumber())
                .status(account.getAccountStatus().getStatus())
                .lastAccessDate(LocalDateTime.now())
                .build();
    }

    @Override
    public String getAccountId(Long seq) {
        ResponseAccountDto responseAccountDto = new ResponseAccountDto();
        responseAccountDto = getAccount(seq);


        return responseAccountDto.getAccountId();
    }

    @Override
    public String getPassword(Long seq) {
        ResponseAccountDto responseAccountDto = new ResponseAccountDto();
        responseAccountDto = getAccount(seq);

        return responseAccountDto.getPassword();
    }

    @Override
    public String getEmail(Long seq) {
        ResponseAccountDto responseAccountDto = new ResponseAccountDto();
        responseAccountDto = getAccount(seq);

        return responseAccountDto.getEmail();
    }

    @Override
    public ResponseAccountDto getAccountByEmail(String email) {
        QAccount account = QAccount.account;
        QAdditionalInfo additionalInfo = QAdditionalInfo.additionalInfo;

        Account result = new JPAQuery<>(entityManager)
                .select(account)
                .from(account)
                .join(account.additionalInfo, additionalInfo)
                .where(additionalInfo.email.eq(email))
                .fetchOne();

        if (result == null) {
            throw new RuntimeException("Account not found");
        }

        return convertToResponseAccountDto(result);
    }

    private ResponseAccountDto convertToResponseAccountDto(Account account) {
        return com.minidooray.accountapi.response.ResponseAccountDto.builder()
                .accountSeq(account.getSeq())
                .accountId(account.getId())
                .password(account.getPassword())
                .name(account.getName())
                .email(account.getAdditionalInfo().getEmail())
                .phoneNumber(account.getAdditionalInfo().getPhoneNumber())
                .status(account.getAccountStatus().getStatus())
                .lastAccessDate(account.getAccountStatus().getAccessDate())
                .build();
    }

    @Override
    public boolean existEmail(String email) {
        QAccount account = QAccount.account;
        QAdditionalInfo additionalInfo = QAdditionalInfo.additionalInfo;

        Long count = new JPAQuery<>(entityManager)
                .select(account.count())
                .from(account)
                .join(account.additionalInfo, additionalInfo)
                .where(additionalInfo.email.eq(email))
                .fetchOne();

        return count != null && count > 0;
    }

    @Override
    public boolean existId(String id) {
        QAccount account = QAccount.account;

        Long count = new JPAQuery<>(entityManager)
                .select(account.count())
                .from(account)
                .where(account.id.eq(id))
                .fetchOne();

        return count != null && count > 0;
    }

    @Override
    public ResponseAccountDto getAccountById(String id) {
        QAccount account = QAccount.account;

        Account result = new JPAQuery<>(entityManager)
                .select(account)
                .from(account)
                .where(account.id.eq(id))
                .fetchOne();

        if (result == null) {
            throw new RuntimeException("Account not found");
        }

        return convertToResponseAccountDto(result);
    }


    @Override
    public boolean existLoginAccount(String id, String password) {
        ResponseAccountDto account = getAccountById(id);
        return account != null && account.getPassword().equals(password);
    }





}

