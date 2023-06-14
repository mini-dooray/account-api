package com.minidooray.accountapi.service.impl;

import com.minidooray.accountapi.entity.*;
import com.minidooray.accountapi.repository.AccountRepository;
import com.minidooray.accountapi.repository.AccountStatusRepository;
import com.minidooray.accountapi.repository.AdditionalInfoRepository;
import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import com.minidooray.accountapi.service.AccountService;
import com.minidooray.accountapi.service.impl.AccountServiceImpl;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    private EntityManager entityManager;

    @Mock
    private AccountServiceImpl accountService;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private JPAQueryFactory queryFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entityManager = mock(EntityManager.class);
        accountRepository = mock(AccountRepository.class);
        accountStatusRepository = mock(AccountStatusRepository.class);
        additionalInfoRepository = mock(AdditionalInfoRepository.class);
        accountService = new AccountServiceImpl(accountRepository, accountStatusRepository, additionalInfoRepository, entityManagerFactory);
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


        assertEquals(account.getId(), response.getAccountId());
        assertEquals(account.getPassword(), response.getPassword());
        assertEquals(account.getName(), response.getName());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(request.getStatus(), response.getStatus());
        assertEquals(request.getLastAccessDate(), response.getLastAccessDate());

        assertNotNull(account);
        assertNull(response);

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
        String id = "TSe123";
        String password = "123456";
        String email = "Testt55@Test.com";
        Long seq = 80L;
        Account account = new Account();
        account.setId(id);
        account.setSeq(seq);
        account.setPassword(password);
        account.setName("TestUser12");

        AdditionalInfo info = new AdditionalInfo();
        info.setEmail(email);
        info.setPhoneNumber("010-0000-0000");
        info.setSeq(24L);

        AccountStatus status = new AccountStatus();
        status.setSeq(24L);
        status.setAccessDate(LocalDate.now());
        status.setStatus(1);

        account.setAccountStatus(status);
        account.setAdditionalInfo(info);

        QAccount qAccount = QAccount.account;
        QAdditionalInfo qAdditionalInfo = QAdditionalInfo.additionalInfo;
        JPAQuery<Account> queryMock = Mockito.mock(JPAQuery.class);
        TypedQuery<Account> typedQuery = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Account.class))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(account);
        when(queryMock.select(qAccount)).thenReturn(queryMock);
        when(queryMock.from(qAccount)).thenReturn(queryMock);
        when(queryMock.join(qAccount.additionalInfo, qAdditionalInfo)).thenReturn(queryMock);
        when(queryMock.where(qAdditionalInfo.email.eq(email))).thenReturn(queryMock);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(account));


        ResponseAccountDto result = convertToResponseAccountDto(account);

        assertEquals(account.getSeq(), result.getAccountSeq());
        assertEquals(account.getId(), result.getAccountId());
        assertEquals(account.getPassword(), result.getPassword());
        assertEquals(account.getName(), result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(account.getAdditionalInfo().getPhoneNumber(), result.getPhoneNumber());
        assertEquals(account.getAccountStatus().getStatus(), result.getStatus());
        assertEquals(account.getAccountStatus().getAccessDate(), result.getLastAccessDate());
    }

    @Test
    void testExistEmail() {
        //실제로 들어가 있어야만 돌아감.
        String email = "alice@example.com";

        QAccount account = QAccount.account;
        QAdditionalInfo additionalInfo = QAdditionalInfo.additionalInfo;

        Long count = 1L;

        JPAQuery<Long> queryMock = Mockito.mock(JPAQuery.class);
        when(queryMock.select(account.count())).thenReturn(queryMock);
        when(queryMock.from(account)).thenReturn(queryMock);
        when(queryMock.join(account.additionalInfo, additionalInfo)).thenReturn(queryMock);
        when(queryMock.where(account.additionalInfo.email.eq(email))).thenReturn(queryMock);
        when(queryMock.fetchOne()).thenReturn(count);

        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository, accountStatusRepository, additionalInfoRepository, entityManagerFactory);
        boolean result = accountService.existEmail(email);
        assertTrue(result);
    }

    @Test
    void testExistId() {
        //실제로 들어가 있어야만 돌아감.
        String id = "user1";

        QAccount qAccount = QAccount.account;

        Long count = 1L;

        JPAQueryFactory queryFactory = Mockito.mock(JPAQueryFactory.class);
        JPAQuery<Long> queryMock = Mockito.mock(JPAQuery.class);

        when(queryFactory.select(qAccount.count())).thenReturn(queryMock);
        when(queryMock.from(qAccount)).thenReturn(queryMock);
        when(queryMock.where(qAccount.id.eq(id))).thenReturn(queryMock);
        when(queryMock.fetchOne()).thenReturn(count);

        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository, accountStatusRepository, additionalInfoRepository, entityManagerFactory);
        boolean result = accountService.existId(id);
        assertTrue(result);
    }

    @Test
    void testGetAccountById() {
        //db에서 데이터를 가져옴, db와 맞춰야함.
        String id = "user1";
        String password = "password1";

        Account account = new Account();
        account.setPassword(password);
        account.setId(id);
        account.setName("John");
        account.setSeq(1L);

        AccountStatus status = new AccountStatus();
        status.setStatus(1);
        status.setAccessDate(LocalDate.now());
        account.setAccountStatus(status);

        AdditionalInfo info = new AdditionalInfo();
        info.setPhoneNumber("1234567890");
        info.setEmail("john@example.com");
        account.setAdditionalInfo(info);

        QAccount qAccount = QAccount.account;
        QAdditionalInfo qAdditionalInfo = QAdditionalInfo.additionalInfo;
        JPAQuery<Account> queryMock = Mockito.mock(JPAQuery.class);
        TypedQuery<Account> typedQuery = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Account.class))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(account);
        when(queryMock.select(qAccount)).thenReturn(queryMock);
        when(queryMock.from(qAccount)).thenReturn(queryMock);
        when(queryMock.join(qAccount.additionalInfo, qAdditionalInfo)).thenReturn(queryMock);
        when(queryMock.where(qAccount.id.eq(id))).thenReturn(queryMock);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(account));


//        ResponseAccountDto result = convertToResponseAccountDto(account);
        ResponseAccountDto result = accountService.getAccountById(id);

        assertEquals(account.getSeq(), result.getAccountSeq());
        assertEquals(id, result.getAccountId());
        assertEquals(account.getPassword(), result.getPassword());
        assertEquals(account.getName(), result.getName());
        assertEquals(account.getAdditionalInfo().getEmail(), result.getEmail());
        assertEquals(account.getAdditionalInfo().getPhoneNumber(), result.getPhoneNumber());
        assertEquals(account.getAccountStatus().getStatus(), result.getStatus());
        assertEquals(account.getAccountStatus().getAccessDate(), result.getLastAccessDate());
    }

    @Test
    void testConvertToResponseAccountDto() {
        String id = "TSe123";
        String password = "123456";
        Long seq = 80L;
        Account account = new Account();
        account.setId(id);
        account.setSeq(seq);
        account.setPassword(password);
        account.setName("TestUser12");

        AdditionalInfo info = new AdditionalInfo();
        info.setEmail("Testt55@Test.com");
        info.setPhoneNumber("010-0000-0000");
        info.setSeq(24L);

        AccountStatus status = new AccountStatus();
        status.setSeq(24L);
        status.setAccessDate(LocalDate.now());
        status.setStatus(1);

        account.setAccountStatus(status);
        account.setAdditionalInfo(info);

        ResponseAccountDto result = convertToResponseAccountDto(account);

        assertEquals(account.getSeq(), result.getAccountSeq());
        assertEquals(account.getId(), result.getAccountId());
        assertEquals(account.getPassword(), result.getPassword());
        assertEquals(account.getName(), result.getName());

        AdditionalInfo additionalInfo = account.getAdditionalInfo();
        if (additionalInfo != null) {
            assertEquals(additionalInfo.getEmail(), result.getEmail());
            assertEquals(additionalInfo.getPhoneNumber(), result.getPhoneNumber());
        } else {
            assertNull(result.getEmail());
            assertNull(result.getPhoneNumber());
        }

        AccountStatus accountStatus = account.getAccountStatus();
        if (accountStatus != null) {
            assertEquals(accountStatus.getStatus(), result.getStatus());
            assertEquals(accountStatus.getAccessDate(), result.getLastAccessDate());
        } else {
            assertNull(result.getStatus());
            assertNull(result.getLastAccessDate());
        }
    }

    @Test
    void testExistLoginAccount() {
        String id = "TSe123";
        String password = "123456";

        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository, accountStatusRepository, additionalInfoRepository, entityManagerFactory);

        AccountServiceImpl accountServiceMock = Mockito.spy(accountService);
        Mockito.doNothing().when(accountServiceMock).setAccess(Mockito.anyString(), Mockito.anyString());

        boolean result = accountServiceMock.existLoginAccount(id, password);

        verify(accountServiceMock, times(1)).getAccountById(id);
        verify(accountServiceMock, times(1)).setAccess(id, password);

        assertTrue(result);
    }
    @Test
    void testAccessTimeCalculator() {

        Account account = mock(Account.class);
        AccountStatus status = mock(AccountStatus.class);
        LocalDate lastAccessDate = LocalDate.now().minusYears(1);

        when(account.getAccountStatus()).thenReturn(status);
        when(status.getAccessDate()).thenReturn(lastAccessDate);
        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(account);

        accountService.accessTimeCalculator(account);

        verify(status).setStatus(3);
    }

    @Test
    void testUpdateAccessDate() {
        Account account = mock(Account.class);
        AccountStatus status = mock(AccountStatus.class);
        LocalDate currentDate = LocalDate.now();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(account));
        when(account.getAccountStatus()).thenReturn(status);
        when(status.getAccessDate()).thenReturn(currentDate);
        when(account.getAdditionalInfo()).thenReturn(mock(AdditionalInfo.class));

        ResponseAccountDto response = accountService.updateAccessDate(1L);

        verify(accountRepository).saveAndFlush(account);

        assertEquals(currentDate, status.getAccessDate());
        assertEquals(currentDate, response.getLastAccessDate());
    }

    @Test
    void testSetAccess(){
        String id = "TSe123";
        String password = "123456";
        Long seq = 80L;
        Account account = new Account();
        account.setId(id);
        account.setSeq(seq);
        account.setPassword(password);
        account.setName("TestUser12");

        AdditionalInfo info = new AdditionalInfo();
        info.setEmail("Testt55@Test.com");
        info.setPhoneNumber("010-0000-0000");
        info.setSeq(24L);

        AccountStatus status = new AccountStatus();
        status.setSeq(24L);
        status.setAccessDate(LocalDate.now());
        status.setStatus(1);

        account.setAccountStatus(status);
        account.setAdditionalInfo(info);

        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository, accountStatusRepository, additionalInfoRepository, entityManagerFactory);


        when(accountRepository.findById(account.getSeq())).thenReturn(Optional.of(account));

        accountService.setAccess(id,password);

//        verify(accountRepository).save(eq(account));

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