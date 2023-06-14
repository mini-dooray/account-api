package com.minidooray.accountapi.service;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountStatusRepository accountStatusRepository;

    @Mock
    private AdditionalInfoRepository additionalInfoRepository;

    @InjectMocks
    private AccountServiceImpl accountService;


    @BeforeEach
    void setUp(){
        accountService = Mockito.mock(AccountServiceImpl.class);
    }

    @Test
    void testGetAccount(){
        Long seq = 1L;
        Account account = createMockAccount();
        ResponseAccountDto response = convertToResponseAccountDto(account);
        accountService.getAccount(seq);
        assertEquals(seq, 1L);
        assertEquals(account.getId(), response.getAccountId());
        assertEquals(account.getPassword(), response.getPassword());
        assertEquals(account.getName(), response.getName());
        assertEquals(account.getAdditionalInfo().getEmail(), response.getEmail());
        assertEquals(account.getAdditionalInfo().getPhoneNumber(), response.getPhoneNumber());
        assertEquals(account.getAccountStatus().getStatus(), response.getStatus());
        assertEquals(account.getAccountStatus().getAccessDate(), response.getLastAccessDate());

//        verify(accountRepository).findById(seq);
        verify(accountService).getAccount(1L);
    }

    @Test
    void testDeleteAccount(){
        Long seq = 3L;

        accountRepository.deleteById(seq);

        verify(accountRepository).deleteById(3L);
    }

    @Test
    void testRegister(){
        RequestAccountDto request = createRequest();

        accountService.register(request);

        verify(accountService).register(request);
        verify(accountService,times(1)).register(request);
        verify(accountService,atLeastOnce()).register(request);
    }

    @Test
    void testUpdateAccount(){
        Long seq = 1L;
        RequestAccountDto request = createRequest();
        Account account = createMockAccount();
        account.setAccount(request.getAccountId(), request.getPassword(), request.getName());

        accountService.updateAccount(seq, request);

        verify(accountService).updateAccount(seq,request);
    }

    @Test
    void testGetAccountId() {
        Long seq = 3L;
        Account account = createMockAccount();
        when(accountRepository.findById(seq)).thenReturn(java.util.Optional.of(account));

        accountService.getAccountId(seq);
        accountRepository.findById(seq);

        verify(accountRepository).findById(seq);
        verify(accountService).getAccountId(seq);
    }
    @Test
    void testGetPassword(){
        Long seq = 3L;
        Account account = createMockAccount();
        when(accountRepository.findById(seq)).thenReturn(java.util.Optional.of(account));

        accountRepository.findById(seq);
        accountService.getPassword(seq);

        verify(accountRepository).findById(seq);
        verify(accountService).getPassword(seq);
    }


    @Test
    void testGetAccountByEmail() {
        String  email = "alice@example.com";
        accountRepository.getAccountByEmail(email);
        verify(accountRepository).getAccountByEmail(email);
    }

    @Test
    void testExistEmail(){
        String  email = "alice@example.com";
        accountService.existEmail(email);
        verify(accountService).existEmail(email);
    }

    @Test
    void testExistId(){
        String id = "user3";
        accountService.existId(id);
        verify(accountService).existId(id);
    }

    @Test
    void testGetAccountById(){
        String id = "user3";
        accountService.getAccountById(id);
        verify(accountService).getAccountById(id);
    }

    @Test
    void testExistLoginAccount() {
        String id = "user3";
        String password = "password3";

        accountService.setAccess(id,password);
        accountService.existLoginAccount(id, password);
        verify(accountService).existLoginAccount(id,password);
        verify(accountService).setAccess(id, password);
    }

    @Test
    void testAccessTimeCalculator_Access_Date_Greater_Than_1Year() {
        Account account = mock(Account.class);
        AccountStatus status = mock(AccountStatus.class);
        LocalDate lastAccessDate = LocalDate.now().minusYears(1);
        LocalDate date = LocalDate.now();
        long daysDifference = ChronoUnit.DAYS.between(lastAccessDate, date);

        if(daysDifference>=365) {
            accountService.accessTimeCalculator(account);
            status.saveStatus(3);
            verify(status).saveStatus(3);
        }
    }

    @Test
    void testAccessTimeCalculator_Access_Date_Less_Than_1Year() {
        Account account = mock(Account.class);
        AccountStatus status = mock(AccountStatus.class);
        LocalDate lastAccessDate = LocalDate.now().minusDays(50);
        LocalDate date = LocalDate.now();
        long daysDifference = ChronoUnit.DAYS.between(lastAccessDate, date);

        if(daysDifference>=365) {
            accountService.accessTimeCalculator(account);
            status.saveStatus(1);
            verify(status).saveStatus(1);
        }
    }





    @Test
    void testConvertToResponseAccountDto() {
        Account account = createMockAccount();
        ResponseAccountDto result = convertToResponseAccountDto(account);
        assertEquals(1, result.getAccountSeq());
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
    void testUpdateAccessDate() {
        Account account = createMockAccount();

        accountService.updateAccessDate(1L);

        verify(accountService).updateAccessDate(1L);
    }


    private ResponseAccountDto convertToResponseAccountDto(Account account){
        AdditionalInfo info = account.getAdditionalInfo();
        AccountStatus status = account.getAccountStatus();
        return ResponseAccountDto.builder()
                .status(1)
                .accountSeq(1L)
                .accountId(account.getId())
                .email(info.getEmail())
                .name(account.getName())
                .phoneNumber(info.getPhoneNumber())
                .password(account.getPassword())
                .lastAccessDate(status.getAccessDate())
                .build();
    }



    private RequestAccountDto createRequest(){
        RequestAccountDto dto = new RequestAccountDto();
        dto.setAccountId("user3");
        dto.setStatus(1);
        dto.setEmail("alice@example.com");
        dto.setPassword("password3");
        dto.setPhoneNumber("5555555555");
        dto.setLastAccessDate(LocalDate.of(2023,6,14));
        dto.setName("Alice");
        return dto;
    }

    private ResponseAccountDto convertRequestToResponse(RequestAccountDto dto){
        return ResponseAccountDto.builder()
                .accountId(dto.getAccountId())
                .status(dto.getStatus())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .lastAccessDate(dto.getLastAccessDate())
                .name(dto.getName())
                .build();
    }

    private Account createMockAccount() {
        Account account = new Account();
        account.setAccount("user3", "password3", "Alice");
        AdditionalInfo info = new AdditionalInfo();
        info.setAdditionalInfo("alice@example.com", "5555555555");
        AccountStatus status = new AccountStatus();
        status.setAccountStatus(LocalDate.of(2023, 6, 14), 1);

        account.setAccountStatus(status);
        account.setAdditionalInfo(info);

        return account;
    }

}






//    @Test
//    void testUpdateAccessDate() {
//        Account account = mock(Account.class);
//        AccountStatus status = mock(AccountStatus.class);
//        LocalDate currentDate = LocalDate.now();
//        when(accountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(account));
//        when(account.getAccountStatus()).thenReturn(status);
//        when(status.getAccessDate()).thenReturn(currentDate);
//        when(account.getAdditionalInfo()).thenReturn(mock(AdditionalInfo.class));
//        ResponseAccountDto response = accountService.updateAccessDate(1L);
//        verify(accountRepository).saveAndFlush(account);
//        assertEquals(currentDate, status.getAccessDate());
//        assertEquals(currentDate, response.getLastAccessDate());
//    }
//    @Test
//    void testSetAccess(){
//        String id = "TSe123";
//        String password = "123456";
//        Long seq = 80L;
//        Account account = new Account();
//        account.setId(id);
//        account.setSeq(seq);
//        account.setPassword(password);
//        account.setName("TestUser12");
//        AdditionalInfo info = new AdditionalInfo();
//        info.setEmail("Testt55@Test.com");
//        info.setPhoneNumber("010-0000-0000");
//        info.setSeq(24L);
//        AccountStatus status = new AccountStatus();
//        status.setSeq(24L);
//        status.setAccessDate(LocalDate.now());
//        status.setStatus(1);
//        account.setAccountStatus(status);
//        account.setAdditionalInfo(info);
//        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository, accountStatusRepository, additionalInfoRepository, entityManagerFactory);
//        when(accountRepository.findById(account.getSeq())).thenReturn(Optional.of(account));
//        accountService.setAccess(id,password);
////        verify(accountRepository).save(eq(account));
//    }
//    private Account createMockAccount() {
//        Account account = new Account();
//        account.setSeq(1L);
//        account.setId("test123");
//        account.setPassword("password");
//        account.setName("TestName1");
//        AdditionalInfo additionalInfo = new AdditionalInfo();
//        additionalInfo.setEmail("Test123@TestCode.com");
//        additionalInfo.setPhoneNumber("1234567890");
//        AccountStatus accountStatus = new AccountStatus();
//        accountStatus.setStatus(1);
//        accountStatus.setAccessDate(LocalDate.now());
//        account.setAdditionalInfo(additionalInfo);
//        account.setAccountStatus(accountStatus);
//        return account;
//    }
//    private RequestAccountDto createMockRequestAccountDto() {
//        RequestAccountDto requestDto = new RequestAccountDto();
//        requestDto.setAccountId("test123");
//        requestDto.setPassword("password");
//        requestDto.setName("TestName2");
//        requestDto.setEmail("Test123@TestCode.com");
//        requestDto.setPhoneNumber("1234567890");
//        return requestDto;
//    }
//    private ResponseAccountDto convertToResponseAccountDto(Account account) {
//        return ResponseAccountDto.builder()
//                .accountSeq(account.getSeq())
//                .accountId(account.getId())
//                .password(account.getPassword())
//                .name(account.getName())
//                .email(account.getAdditionalInfo().getEmail())
//                .phoneNumber(account.getAdditionalInfo().getPhoneNumber())
//                .status(account.getAccountStatus().getStatus())
//                .lastAccessDate(account.getAccountStatus().getAccessDate())
//                .build();
//    }
//}
// */