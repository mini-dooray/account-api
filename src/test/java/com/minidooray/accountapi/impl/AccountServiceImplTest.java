package com.minidooray.accountapi.impl;


public class AccountServiceImplTest {

//    @InjectMocks
//    private AccountServiceImpl accountService;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAccount() {
//        Long seq = 1L;
//        Account account = new Account();
//        account.setSeq(seq);
//        account.setId("test");
//        account.setPassword("1234");
//        account.setName("TestUser");
//
//        AdditionalInfo additionalInfo = new AdditionalInfo();
//        additionalInfo.setEmail("test@test.com");
//        additionalInfo.setPhoneNumber("010-1234-5678");
//        additionalInfo.setAccount(account);
//        account.setAdditionalInfo(additionalInfo);
//
//        AccountStatus accountStatus = new AccountStatus();
//        accountStatus.setStatus(1);
//        accountStatus.setAccessDate(LocalDateTime.now());
//        accountStatus.setAccount(account);
//        account.setAccountStatus(accountStatus);
//
//        when(accountRepository.findById(seq)).thenReturn(Optional.of(account));
//
//        ResponseAccountDto result = accountService.getAccount(seq);
//
//        assertEquals(seq, result.getAccountSeq());
//        assertEquals("test", result.getAccountId());
//        assertEquals("1234", result.getPassword());
//        assertEquals("TestUser", result.getName());
//        assertEquals("test@test.com", result.getEmail());
//        assertEquals("010-1234-5678", result.getPhoneNumber());
//        assertEquals(1, result.getStatus());
//        assertEquals(accountStatus.getAccessDate(), result.getLastAccessDate());
//    }
}