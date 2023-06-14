package com.minidooray.accountapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;


    @Test
    void testGetAccount_Success() throws Exception {
        Long seq = 1L;
        Account account = createMockAccount();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(accountService.getAccount(seq)).thenReturn(convertToResponseAccountDto(account));

        mockMvc.perform(get("/account/{seq}", seq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(convertToResponseAccountDto(account))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountSeq").value(1L))
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
    void testGetAccount_Failure() throws Exception {
        Long seq = 1L;
        Account account = createMockAccount();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        when(accountService.getAccount(seq)).thenReturn(convertToResponseAccountDto(account));

        mockMvc.perform(get("/account/{seq}", seq)
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.accountSeq").value(1L))
                .andExpect(jsonPath("$.accountId").value(account.getId()))
                .andExpect(jsonPath("$.password").value(account.getPassword()))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.email").value(account.getAdditionalInfo().getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(account.getAdditionalInfo().getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(account.getAccountStatus().getStatus()))
                .andExpect(jsonPath("$.lastAccessDate").value(account.getAccountStatus().getAccessDate().toString()));
    }
    @Test
    void testRegister_Success() throws  Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        RequestAccountDto dto = createRequest();
        accountService.register(dto);
        Account account = createMockAccount();

        mockMvc.perform(post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(account)))
                .andExpect(status().isCreated());
    }
    @Test
    void testRegister_Failure() throws  Exception{
        mockMvc.perform(post("/account/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteAccount() throws Exception {
        Long seq = 1L;
        Account account = createMockAccount();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mockMvc.perform(post("/account/delete/1",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAccount() throws Exception{
        Long seq = 1L;
        RequestAccountDto dto = createRequest();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mockMvc.perform(put("/account/update/1",seq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAccountId() throws Exception{
        RequestAccountDto requestAccountDto = createRequest();
        ResponseAccountDto dto = convertRequestToResponse(requestAccountDto);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        when(accountService.getAccountId(1L)).thenReturn(requestAccountDto.getAccountId());
        mockMvc.perform(get("/account/user/1",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(dto.getAccountId()));
    }

    @Test
    void testGetEmail() throws Exception{
        RequestAccountDto requestAccountDto = createRequest();
        ResponseAccountDto dto = convertRequestToResponse(requestAccountDto);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        when(accountService.getEmail(1L)).thenReturn(requestAccountDto.getEmail());

        mockMvc.perform(get("/account/email/1",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(dto.getEmail()));
    }

    @Test
    void testGetPassword() throws Exception{
        RequestAccountDto requestAccountDto = createRequest();
        ResponseAccountDto dto = convertRequestToResponse(requestAccountDto);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        when(accountService.getPassword(1L)).thenReturn(requestAccountDto.getPassword());

        mockMvc.perform(get("/account/pwd/1",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(dto.getPassword()));
    }

    @Test
    void testGeAccountByEmail() throws Exception{
        Account account = createMockAccount();
        String email = "test@test.com";
        when(accountService.getAccountByEmail(email)).thenReturn(convertToResponseAccountDto(account));
        mockMvc.perform(get("/account/{email}/found", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountSeq").value(1L))
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
        String id = "TestId";
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
        RequestAccountDto requestDto = createRequest();
        String accountId = requestDto.getAccountId();
        String password = requestDto.getPassword();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        boolean result = true;
        when(accountService.existLoginAccount(accountId, password)).thenReturn(result);
        mockMvc.perform(post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("로그인 성공"));
        boolean loginSuccess = accountService.existLoginAccount(accountId, password);
        assertTrue(loginSuccess);
    }
    @Test
    void testLogin_Fail() throws Exception {
        RequestAccountDto requestDto = createRequest();
        String accountId = requestDto.getAccountId();
        String password = requestDto.getPassword();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        boolean result = false;
        when(accountService.existLoginAccount(accountId, password)).thenReturn(result);
        mockMvc.perform(post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
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

    private Account createMockAccount() {
        Account account = new Account();
        account.setAccount("TestId", "123456", "테스트이름");
        AdditionalInfo info = new AdditionalInfo();
        info.setAdditionalInfo("TestEmail@@Test.com", "010-000-0000");
        AccountStatus status = new AccountStatus();
        status.setAccountStatus(LocalDate.of(2023, 6, 14), 1);

        account.setAccountStatus(status);
        account.setAdditionalInfo(info);

        return account;
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
        dto.setAccountId("TestId");
        dto.setStatus(1);
        dto.setEmail("TestEmail@@Test.com");
        dto.setPassword("123456");
        dto.setPhoneNumber("010-000-0000");
        dto.setLastAccessDate(LocalDate.of(2023,6,14));
        dto.setName("테스트이름");
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
    void AccessTimeCalculator(Account account){
        LocalDate lastAccessDate = account.getAccountStatus().getAccessDate();
        LocalDate currentDate = LocalDate.now();
        AccountStatus status = account.getAccountStatus();

        long daysDifference = ChronoUnit.DAYS.between(lastAccessDate, currentDate);

        if(daysDifference>=365){
            status.saveStatus(3);
        }
    }


}
