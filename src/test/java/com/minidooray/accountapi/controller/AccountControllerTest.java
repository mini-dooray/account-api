package com.minidooray.accountapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.minidooray.accountapi.entity.Account;
import com.minidooray.accountapi.entity.AccountStatus;
import com.minidooray.accountapi.entity.AdditionalInfo;
import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import com.minidooray.accountapi.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountController accountController;

    @MockBean
    private AccountService accountService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController(accountService);
    }

    @Test
    void testGetAccount() throws Exception {
        Long seq = 1L;
        Account account = createMockAccount();
        AccessTimeCalculator(account);

        when(accountService.getAccount(seq)).thenReturn(convertToResponseAccountDto(account));

        mockMvc.perform(get("/account/{seq}", seq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountSeq").value(account.getSeq()))
                .andExpect(jsonPath("$.accountId").value(account.getId()))
                .andExpect(jsonPath("$.password").value(account.getPassword()))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.email").value(account.getAdditionalInfo().getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(account.getAdditionalInfo().getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(account.getAccountStatus().getStatus()))
                .andExpect(jsonPath("$.lastAccessDate").value(account.getAccountStatus().getAccessDate().toString()));

        verify(accountService, times(1)).getAccount(seq);
    }

    @Test
    void testRegister() throws Exception {
        RequestAccountDto requestDto = createMockRequestAccountDto();
        Account account = createMockAccount();

        when(accountService.register(any(RequestAccountDto.class))).thenReturn(convertToResponseAccountDto(account));
        ResponseEntity<Void> responseEntity = accountController.register(requestDto);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteAccount() throws Exception {
        Long seq = 1L;
        Account account = createMockAccount();
        ResponseAccountDto responseAccountDto = convertToResponseAccountDto(account);

        when(accountService.deleteAccount(seq)).thenReturn(responseAccountDto);

        mockMvc.perform(post("/account/delete/{seq}", seq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountSeq").value(responseAccountDto.getAccountSeq()))
                .andExpect(jsonPath("$.accountId").value(responseAccountDto.getAccountId()))
                .andExpect(jsonPath("$.password").value(responseAccountDto.getPassword()))
                .andExpect(jsonPath("$.name").value(responseAccountDto.getName()))
                .andExpect(jsonPath("$.email").value(responseAccountDto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(responseAccountDto.getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(responseAccountDto.getStatus()))
                .andExpect(jsonPath("$.lastAccessDate").value(responseAccountDto.getLastAccessDate().toString()));

        verify(accountService, times(1)).deleteAccount(seq);
    }
    @Test
    void testUpdateAccount() throws Exception {

        RequestAccountDto requestDto = createMockRequestAccountDto();

        ResponseAccountDto responseDto = new ResponseAccountDto();
        responseDto.setAccountSeq(1L);
        responseDto.setAccountId(requestDto.getAccountId());
        responseDto.setPassword(requestDto.getPassword());
        responseDto.setName(requestDto.getName());
        responseDto.setEmail(requestDto.getEmail());
        responseDto.setPhoneNumber(requestDto.getPhoneNumber());
        responseDto.setStatus(requestDto.getStatus());
        responseDto.setLastAccessDate(LocalDate.now());

        when(accountService.updateAccount(anyLong(), any(RequestAccountDto.class))).thenReturn(responseDto);
        mockMvc.perform(put("/account/update/{seq}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountSeq").value(responseDto.getAccountSeq()))
                .andExpect(jsonPath("$.accountId").value(responseDto.getAccountId()))
                .andExpect(jsonPath("$.password").value(responseDto.getPassword()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(responseDto.getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(responseDto.getStatus()))
                .andExpect(jsonPath("$.lastAccessDate").exists());
    }

    @Test
    void testGetAccountId() throws Exception {
        Long seq = 1L;
        String accountId = "test123";
        when(accountService.getAccountId(seq)).thenReturn(accountId);
        mockMvc.perform(get("/account/user/{seq}", seq))
                .andExpect(status().isOk())
                .andExpect(content().string(accountId));
    }

    @Test
    void testGetEmail() throws Exception {
        Long seq = 1L;
        String email = "test@test.com";
        when(accountService.getEmail(seq)).thenReturn(email);
        mockMvc.perform(get("/account/email/{seq}", seq))
                .andExpect(status().isOk())
                .andExpect(content().string(email));
    }

    @Test
    void testGetPassword() throws Exception {
        Long seq = 1L;
        String password = "password123";
        when(accountService.getPassword(seq)).thenReturn(password);
        mockMvc.perform(get("/account/pwd/{seq}", seq))
                .andExpect(status().isOk())
                .andExpect(content().string(password));
    }

    @Test
    void testGetAccountByEmail() throws Exception {
        String email = "test@test.com";
        Account account = createMockAccount();
        when(accountService.getAccountByEmail(email)).thenReturn(convertToResponseAccountDto(account));

        mockMvc.perform(get("/account/{email}/found", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountSeq").value(account.getSeq()))
                .andExpect(jsonPath("$.accountId").value(account.getId()))
                .andExpect(jsonPath("$.password").value(account.getPassword()))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.email").value(account.getAdditionalInfo().getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(account.getAdditionalInfo().getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(account.getAccountStatus().getStatus()))
                .andExpect(jsonPath("$.lastAccessDate").exists());
    }
    @Test
    void testExistsEmail() throws Exception{
        String email = "Test123123@Test.com";
        when(accountService.existEmail(email)).thenReturn(true);

        mockMvc.perform(get("/account/find/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testExistsId() throws Exception{
        String id = "TestId123";
        when(accountService.existId(id)).thenReturn(true);

        mockMvc.perform(get("/account/find/id/{id}",id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetAccountById() throws Exception {
        String id = "test123";
        Account account = createMockAccount();
        ResponseAccountDto responseDto = convertToResponseAccountDto(account);

        when(accountService.getAccountById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/account/account/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(account.getId()))
                .andExpect(jsonPath("$.password").value(account.getPassword()))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.email").value(account.getAdditionalInfo().getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(account.getAdditionalInfo().getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(account.getAccountStatus().getStatus()))
                .andExpect(jsonPath("$.lastAccessDate").exists());
    }

    @Test
    void testLogin_Success() throws Exception {
        RequestAccountDto requestDto = createMockRequestAccountDto();
        String accountId = requestDto.getAccountId();
        String password = requestDto.getPassword();
        boolean result = true;

        when(accountService.existLoginAccount(accountId, password)).thenReturn(result);

        mockMvc.perform(post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("로그인 성공"));

        boolean loginSuccess = accountService.existLoginAccount(accountId, password);

        assertTrue(loginSuccess);
    }

    @Test
    void testLogin_Fail() throws Exception {
        RequestAccountDto requestDto = createMockRequestAccountDto();
        String accountId = requestDto.getAccountId();
        String password = requestDto.getPassword();
        boolean result = false;

        when(accountService.existLoginAccount(accountId, password)).thenReturn(result);

        mockMvc.perform(post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("로그인 실패"));

        boolean loginFailure = accountService.existLoginAccount(accountId, password);

        assertFalse(loginFailure);
    }

    @Test
    void testUpdateDate_Success() {

        AccountService accountService = mock(AccountService.class);
        AccountController accountController = new AccountController(accountService);

        Long seq = 1L;
        ResponseEntity<Account> response = accountController.updateDate(seq);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(accountService).updateAccessDate(seq);
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

    private Account createMockAccount() {
        Account account = new Account();
        account.setSeq(1L);
        account.setId("test123");
        account.setPassword("password");
        account.setName("TestName1");

        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setEmail("Test@Test.com");
        additionalInfo.setPhoneNumber("1234567890");

        AccountStatus accountStatus = new AccountStatus();
        accountStatus.setStatus(1);
        accountStatus.setAccessDate(LocalDate.now());

        account.setAdditionalInfo(additionalInfo);
        account.setAccountStatus(accountStatus);

        return account;
    }

    void AccessTimeCalculator(Account account){
        LocalDate lastAccessDate = account.getAccountStatus().getAccessDate();
        LocalDate currentDate = LocalDate.now();
        AccountStatus status = account.getAccountStatus();

        long daysDifference = ChronoUnit.DAYS.between(lastAccessDate, currentDate);

        if(daysDifference>=365){
            status.setStatus(3);
            account.setAccountStatus(status);
        }
    }


}
