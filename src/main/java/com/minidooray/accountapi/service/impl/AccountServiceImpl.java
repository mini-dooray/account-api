package com.minidooray.accountapi.service.impl;

import com.minidooray.accountapi.entity.*;
import com.minidooray.accountapi.repository.AccountRepository;
import com.minidooray.accountapi.repository.AccountStatusRepository;
import com.minidooray.accountapi.repository.AdditionalInfoRepository;
import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import com.minidooray.accountapi.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class AccountServiceImpl  implements AccountService {


    private final AccountRepository accountRepository;
    private final AccountStatusRepository accountStatusRepository;
    private final AdditionalInfoRepository additionalInfoRepository;

    public AccountServiceImpl(AccountRepository accountRepository, AccountStatusRepository accountStatusRepository, AdditionalInfoRepository additionalInfoRepository) {
        this.accountRepository = accountRepository;
        this.accountStatusRepository = accountStatusRepository;
        this.additionalInfoRepository = additionalInfoRepository;
    }


    @Override
    public ResponseAccountDto getAccount(Long seq) {
        Account account = accountRepository.findById(seq)
                .orElse(null);
        accessTimeCalculator(account);

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
    //status 1 = 가입 , 2  = 탈퇴 , 3 = 휴면
    @Override
    @Transactional
    public void deleteAccount(Long seq) {
        accountRepository.deleteById(seq);
    }

    public ResponseAccountDto register(RequestAccountDto request) {

        Account account1 = new Account();
        account1.setAccount(request.getAccountId(),request.getPassword(),request.getName());

        AccountStatus status1 = new AccountStatus();
        AdditionalInfo info1 = new AdditionalInfo();

        info1.setAccountByInfo(account1);
        status1.setAccountByStatus(account1);

        status1.setAccountStatus(LocalDate.now(),1);
        status1.setAccountByStatus(account1);
        info1.setAdditionalInfo(request.getEmail(),request.getPhoneNumber());


        account1.saveInfoAndStatus(status1,info1);

        Account savedAccount = accountRepository.save(account1);
        accountStatusRepository.save(status1);
        additionalInfoRepository.save(info1);


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

        if(account!=null){
            account.setAccount(requestAccountDto.getAccountId(), requestAccountDto.getPassword(), requestAccountDto.getName());

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
        else return null;
    }

    @Override
    public String getAccountId(Long seq) {
        ResponseAccountDto responseAccountDto = getAccount(seq);

        return responseAccountDto.getAccountId();
    }

    @Override
    public String getPassword(Long seq) {
        ResponseAccountDto responseAccountDto = getAccount(seq);

        return responseAccountDto.getPassword();
    }

    @Override
    public String getEmail(Long seq) {
        ResponseAccountDto responseAccountDto = getAccount(seq);

        return responseAccountDto.getEmail();
    }

    @Override
    public ResponseAccountDto getAccountByEmail(String email) {
        Account result = accountRepository.getAccountByEmail(email);
        if (result != null) {
            accessTimeCalculator(result);
            return convertToResponseAccountDto(result);
        } else {
            return null;
        }
    }

    private ResponseAccountDto convertToResponseAccountDto(Account account) {
        ResponseAccountDto dto = ResponseAccountDto.builder()
                .accountSeq(account.getSeq())
                .accountId(account.getId())
                .password(account.getPassword())
                .name(account.getName())
                .build();

        AdditionalInfo additionalInfo = account.getAdditionalInfo();
        if (additionalInfo != null) {
            dto.setEmail(additionalInfo.getEmail());
            dto.setPhoneNumber(additionalInfo.getPhoneNumber());
        }

        AccountStatus accountStatus = account.getAccountStatus();
        if (accountStatus != null) {
            dto.setStatus(accountStatus.getStatus());
            dto.setLastAccessDate(accountStatus.getAccessDate());
        }

        return dto;
    }

    @Override
    public boolean existEmail(String email) {
        Long count = accountRepository.countEmail(email);

        return count != null && count > 0;
    }

    @Override
    public boolean existId(String id) {
        Long count = accountRepository.countId(id);

        return count != null && count > 0;
    }

    @Override
    public ResponseAccountDto getAccountById(String id) {

        Account result = accountRepository.getAccountById(id);
        if(result!=null) {
            accessTimeCalculator(result);
            return convertToResponseAccountDto(result);
        }
        else return null;
    }


    @Transactional
    public boolean existLoginAccount(String id, String password) {
        ResponseAccountDto dto = getAccountById(id);
        setAccess(id,password);
        return dto != null && dto.getPassword().equals(password);
    }

    // 해당 id와 password 를 가지고 있는 계정에 대한 접속일자 수정 메소드
    @Transactional
    public void setAccess(String id, String password) {
        Account result = accountRepository.getAccountByIdAndPassword(id,password);

        if (result != null) {
            AccountStatus status = result.getAccountStatus();
            status.setAccountStatus(LocalDate.now(),1);
        }
    }
    //마지막 접속일자와 최근 접속일자를 계산하여 휴면계정 전환(3)
    public void accessTimeCalculator(Account account) {
        if (account != null) {
            AccountStatus status = account.getAccountStatus();
            if (status != null ) {
                LocalDate lastAccessDate = status.getAccessDate();
                if (lastAccessDate != null) {
                    LocalDate currentDate = LocalDate.now();
                    long daysDifference = ChronoUnit.DAYS.between(lastAccessDate, currentDate);

                    if (daysDifference >= 365) {
                        status.saveStatus(3);
                    }
                }
            }
        }
    }

    public ResponseAccountDto updateAccessDate(Long seq) {
        Account account = accountRepository.findById(seq)
                .orElse(null);

        if (account != null) {
            AccountStatus status = account.getAccountStatus();
            status.saveAccessDate(LocalDate.now());

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
        } else return null;
    }

}

