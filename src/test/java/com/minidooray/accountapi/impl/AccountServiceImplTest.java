package com.minidooray.accountapi.impl;


import com.minidooray.accountapi.controller.AccountController;
import com.minidooray.accountapi.entity.Account;
import com.minidooray.accountapi.entity.AccountStatus;
import com.minidooray.accountapi.entity.AdditionalInfo;
import com.minidooray.accountapi.entity.QAccount;
import com.minidooray.accountapi.repository.AccountRepository;
import com.minidooray.accountapi.repository.AccountStatusRepository;
import com.minidooray.accountapi.repository.AdditionalInfoRepository;
import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import com.minidooray.accountapi.service.AccountService;
import com.minidooray.accountapi.service.impl.AccountServiceImpl;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//@DataJpaTest
//@AutoConfigureTestDatabase
@SpringBootTest
@AutoConfigureMockMvc
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountStatusRepository accountStatusRepository;

    @Mock
    private AdditionalInfoRepository additionalInfoRepository;

//    @Mock
//    private TestEntityManager testEntityManager;
    @Mock
    private EntityManager entityManager;
    @Mock
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
//        entityManager = testEntityManager.getEntityManager();
        accountService = new AccountServiceImpl(accountRepository,accountStatusRepository
        ,additionalInfoRepository,entityManager);
    }
    @Test
    void testGetAccount() {
        Long seq = 1L;
        Account account = createMockAccount();

        when(accountRepository.findById(seq)).thenReturn(java.util.Optional.of(account));
        ResponseAccountDto response = accountService.getAccount(seq);

        assertEquals(seq, response.getAccountSeq());
        assertEquals(account.getId(), response.getAccountId());
        assertEquals(account.getPassword(), response.getPassword());
        assertEquals(account.getName(), response.getName());
        assertEquals(account.getAdditionalInfo().getEmail(), response.getEmail());
        assertEquals(account.getAdditionalInfo().getPhoneNumber(), response.getPhoneNumber());
        assertEquals(account.getAccountStatus().getStatus(), response.getStatus());
        assertEquals(account.getAccountStatus().getAccessDate(), response.getLastAccessDate());
    }

    @Test
    void testDeleteAccount() {
        Long seq = 1L;
        Account account = createMockAccount();

        when(accountRepository.findById(seq)).thenReturn(java.util.Optional.of(account));
        ResponseAccountDto response = accountService.deleteAccount(seq);
        assertEquals(seq, response.getAccountSeq());
        assertEquals(account.getId(), response.getAccountId());
        assertEquals(account.getPassword(), response.getPassword());
        assertEquals(account.getName(), response.getName());
        assertEquals(account.getAdditionalInfo().getEmail(), response.getEmail());
        assertEquals(account.getAdditionalInfo().getPhoneNumber(), response.getPhoneNumber());
        assertEquals(account.getAccountStatus().getStatus(), response.getStatus());
        assertEquals(account.getAccountStatus().getAccessDate(), response.getLastAccessDate());
    }

    @Test
    void testRegister() {

        RequestAccountDto request = createMockRequestAccountDto();

        Long seq = 1L;
        Account savedAccount = createMockAccount();
        AdditionalInfo additionalInfo = savedAccount.getAdditionalInfo();
        AccountStatus status = savedAccount.getAccountStatus();
        savedAccount.setSeq(seq);
        savedAccount.setId(request.getAccountId());
        savedAccount.setPassword(request.getPassword());
        savedAccount.setName(request.getName());
        savedAccount.setAdditionalInfo(additionalInfo);
        savedAccount.setAccountStatus(status);

        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(savedAccount);
        ResponseAccountDto response = accountService.register(request);

        assertEquals(seq, response.getAccountSeq());
        assertEquals(savedAccount.getId(), response.getAccountId());
        assertEquals(savedAccount.getPassword(), response.getPassword());
        assertEquals(savedAccount.getName(), response.getName());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(request.getStatus(), response.getStatus());
        assertEquals(request.getLastAccessDate(), response.getLastAccessDate());
    }

    @Test
    void testUpdate(){
        Long seq = 1L;
        RequestAccountDto request = createMockRequestAccountDto();

        Account account = createMockAccount();
        account.setAccount(request.getAccountId(), request.getPassword(), request.getName());

        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(account);
        ResponseAccountDto response = accountService.updateAccount(seq, request);
        try {

            assertEquals(account.getId(), response.getAccountId());
            assertEquals(account.getPassword(), response.getPassword());
            assertEquals(account.getName(), response.getName());
            assertEquals(request.getEmail(), response.getEmail());
            assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
            assertEquals(request.getStatus(), response.getStatus());
            assertEquals(request.getLastAccessDate(), response.getLastAccessDate());
        }
        catch (Exception e){
            assertNull(response);
        }
    }


    @Test
    void testGetAccountId() {
        Long seq = 1L;
        Account account = createMockAccount();
        when(accountRepository.findById(seq)).thenReturn(java.util.Optional.of(account));
        String accountIdOne = account.getId();
        String accountIdTwo = accountService.getAccountId(seq);
        assertEquals(accountIdOne, accountIdTwo);
    }

    @Test
    void testGetPassword(){
        Long seq = 1L;
        Account account = createMockAccount();
        when(accountRepository.findById(seq)).thenReturn(java.util.Optional.of(account));
        String passwordOne = account.getPassword();
        String passwordTwo = accountService.getPassword(seq);
        assertEquals(passwordOne, passwordTwo);
    }

    @Test
    void testGetEmail(){
        Long seq = 1L;
        Account account = createMockAccount();
        when(accountRepository.findById(seq)).thenReturn(Optional.of(account));
        String EmailOne = account.getAdditionalInfo().getEmail();
        String EmailTwo = accountService.getEmail(seq);
        assertEquals(EmailOne,EmailTwo);
    }

    @Test
    void testGetAccountByEmail() {

        Account account = createMockAccount();
        String email = account.getAdditionalInfo().getEmail();
        ResponseAccountDto expectedDto = convertToResponseAccountDto(account);

        // EntityManagerFactory와 EntityManager에 대한 Mock 생성
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        // EntityManager와 EntityManagerFactory의 동작을 모의화
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManager.createNamedQuery(anyString())).thenReturn(Mockito.mock(Query.class));

        // AccountServiceImpl 인스턴스 생성
        AccountServiceImpl accountService = new AccountServiceImpl(
                accountRepository,
                accountStatusRepository,
                additionalInfoRepository,
                entityManager
        );

        // accountService.getAccountByEmail()의 동작을 모의화
        when(accountService.getAccountByEmail(email)).thenReturn(expectedDto);

        // 테스트할 메소드 호출
        ResponseAccountDto actualDto = accountService.getAccountByEmail(email);

        // 결과 검증
        assertEquals(expectedDto, actualDto);
    }



    private Account createMockAccount() {
        Account account = new Account();
        account.setSeq(1L);
        account.setId("test123");
        account.setPassword("password");
        account.setName("TestName1");

        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setEmail("Test123@TestCode.com");
        additionalInfo.setPhoneNumber("1234567890");

        AccountStatus accountStatus = new AccountStatus();
        accountStatus.setStatus(1);
        accountStatus.setAccessDate(LocalDate.now());

        account.setAdditionalInfo(additionalInfo);
        account.setAccountStatus(accountStatus);

        return account;
    }

    private RequestAccountDto createMockRequestAccountDto() {
        RequestAccountDto requestDto = new RequestAccountDto();
        requestDto.setAccountId("test123");
        requestDto.setPassword("password");
        requestDto.setName("TestName2");
        requestDto.setEmail("Test123@TestCode.com");
        requestDto.setPhoneNumber("1234567890");
        return requestDto;
    }
    private ResponseAccountDto convertToResponseAccountDto(Account account) {
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


}