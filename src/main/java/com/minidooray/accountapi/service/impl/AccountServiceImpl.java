package com.minidooray.accountapi.service.impl;

import com.minidooray.accountapi.entity.*;
import com.minidooray.accountapi.repository.AccountRepository;
import com.minidooray.accountapi.repository.AccountStatusRepository;
import com.minidooray.accountapi.repository.AdditionalInfoRepository;
import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import com.minidooray.accountapi.service.AccountService;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl extends QuerydslRepositorySupport implements AccountService {


    private final AccountRepository accountRepository;
    private final AccountStatusRepository accountStatusRepository;
    private final AdditionalInfoRepository additionalInfoRepository;
    private final EntityManager entityManager;


    private final QAccount account = QAccount.account;
    private final QAdditionalInfo additionalInfo = QAdditionalInfo.additionalInfo;

    public AccountServiceImpl(AccountRepository accountRepository, AccountStatusRepository accountStatusRepository, AdditionalInfoRepository additionalInfoRepository, EntityManager entityManager) {
        super(Account.class);
        this.accountRepository = accountRepository;
        this.accountStatusRepository = accountStatusRepository;
        this.additionalInfoRepository = additionalInfoRepository;
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
        AccountStatus status = new AccountStatus();
        AdditionalInfo info = new AdditionalInfo();
        account.setAccount(request.getAccountId(), request.getPassword(), request.getName());
        Account savedAccount = accountRepository.saveAndFlush(account);
        status.setAccountStatus(request.getLastAccessDate());
        status.setAccount(account);
        info.setAdditionalInfo(request.getEmail(), request.getPhoneNumber());
        info.setAccount(account);
        accountStatusRepository.save(status);
        additionalInfoRepository.save(info);
        account.setData(status, info);


        return ResponseAccountDto.builder()
                .accountSeq(savedAccount.getSeq())
                .accountId(savedAccount.getId())
                .name(savedAccount.getName())
                .password(savedAccount.getPassword())
                .email(savedAccount.getAdditionalInfo().getEmail())
                .phoneNumber(savedAccount.getAdditionalInfo().getPhoneNumber())
                .status(savedAccount.getAccountStatus().getStatus())
                .build();
    }


    public ResponseAccountDto updateAccount(Long seq, RequestAccountDto requestAccountDto) {
        Account account = accountRepository.findById(seq)
                .orElse(null);
        account.setAccount(requestAccountDto.getAccountId(), requestAccountDto.getPassword(), requestAccountDto.getName());

        accountRepository.saveAndFlush(account);

        return ResponseAccountDto.builder()
                .accountSeq(account.getSeq())
                .accountId(account.getId())
                .password(account.getPassword())
                .name(account.getName())
                .email(account.getAdditionalInfo().getEmail())
                .phoneNumber(account.getAdditionalInfo().getPhoneNumber())
                .status(account.getAccountStatus().getStatus())
                .lastAccessDate(LocalDate.now())
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

