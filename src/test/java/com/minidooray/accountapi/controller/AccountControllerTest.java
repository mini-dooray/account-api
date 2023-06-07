package com.minidooray.accountapi.controller;


import com.minidooray.accountapi.controller.AccountController;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
public class AccountControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AccountService accountService;
//
//    @Test
//    void testGetAccount() throws Exception {
//        ResponseAccountDto responseDto = ResponseAccountDto.builder()
//                .accountSeq(1L)
//                .accountId("2")
//                .password("3")
//                .name("4")
//                .email("5")
//                .phoneNumber("6")
//                .lastAccessDate(LocalDateTime.now())
//                .status(1)
//                .build();
//
//        when(accountService.getAccount(anyLong())).thenReturn(responseDto);
//
//        mockMvc.perform(get("/account/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountSeq").exists())
//                .andExpect(jsonPath("$.accountId").exists())
//                .andExpect(jsonPath("$.password").exists())
//                .andExpect(jsonPath("$.name").exists())
//                .andExpect(jsonPath("$.email").exists())
//                .andExpect(jsonPath("$.phoneNumber").exists())
//                .andExpect(jsonPath("$.status").exists())
//                .andExpect(jsonPath("$.lastAccessDate").exists());
//    }
//
//    @Test
//    void testRegister() throws Exception {
//        RequestAccountDto requestDto = new RequestAccountDto(
//                1L,"2","3","4","5","6",LocalDateTime.now(),1
//        );
//        ResponseAccountDto responseDto = new ResponseAccountDto(
//                1L,"2","3","4","5","6",LocalDateTime.now(),1
//        );
//        when(accountService.register(requestDto)).thenReturn(responseDto);
//
//        mockMvc.perform(post("/account/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"accountSeq\": 1, \"accountId\": \"2\", \"password\": \"3\", \"name\": \"4\", \"email\": \"5\", \"phoneNumber\": \"6\", \"status\": 1}")
//        );
//    }
//
//    @Test
//    void testDeleteAccount() throws Exception {
//
//        ResponseAccountDto responseDto = ResponseAccountDto.builder()
//                .accountSeq(1L)
//                .accountId("2")
//                .password("3")
//                .name("4")
//                .email("5")
//                .phoneNumber("6")
//                .lastAccessDate(LocalDateTime.now())
//                .status(1)
//                .build();
//        when(accountService.deleteAccount(anyLong())).thenReturn(responseDto);
//
//        mockMvc.perform(delete("/account/delete/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountSeq").exists())
//                .andExpect(jsonPath("$.accountId").exists())
//                .andExpect(jsonPath("$.password").exists())
//                .andExpect(jsonPath("$.name").exists())
//                .andExpect(jsonPath("$.email").exists())
//                .andExpect(jsonPath("$.phoneNumber").exists())
//                .andExpect(jsonPath("$.status").exists())
//                .andExpect(jsonPath("$.lastAccessDate").exists());
//    }
//
//    @Test
//    void testUpdateAccount() throws Exception {
//
//        ResponseAccountDto responseDto = ResponseAccountDto.builder()
//                .accountSeq(1L)
//                .accountId("2")
//                .password("3")
//                .name("4")
//                .email("5")
//                .phoneNumber("6")
//                .lastAccessDate(LocalDateTime.now())
//                .status(1)
//                .build();
//        when(accountService.updateAccount(anyLong(), any(RequestAccountDto.class))).thenReturn(responseDto);
//
//        mockMvc.perform(put("/account/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accountSeq").exists())
//                .andExpect(jsonPath("$.accountId").exists())
//                .andExpect(jsonPath("$.password").exists())
//                .andExpect(jsonPath("$.name").exists())
//                .andExpect(jsonPath("$.email").exists())
//                .andExpect(jsonPath("$.phoneNumber").exists())
//                .andExpect(jsonPath("$.status").exists())
//                .andExpect(jsonPath("$.lastAccessDate").exists());
//    }
//
//    @Test
//    void testGetAccountId()throws Exception{
//        Long seq = 1L;
//        String AccountId = "Test1";
//        ResponseAccountDto responseAccountDto = new ResponseAccountDto();
//        responseAccountDto.setAccountSeq(seq);
//        responseAccountDto.setAccountId(AccountId);
//        when(accountService.getAccountId(anyLong())).thenReturn(AccountId);
//
//        mockMvc.perform(get("/account/user/{seq}", seq))
//                .andExpect(status().isOk())
//                .andExpect(content().string(AccountId));
//    }
//
//    @Test
//    void testGetPassword() throws Exception {
//        Long seq = 1L;
//        String password = "test123";
//
//        when(accountService.getPassword(seq)).thenReturn(password);
//
//        mockMvc.perform(get("/account/pwd/{seq}", seq))
//                .andExpect(status().isOk())
//                .andExpect(content().string(password));
//    }
//
//    @Test
//    void testGetEmail()throws Exception{
//        Long seq = 1L;
//        String email = "test123@Test123.com";
//
//        when(accountService.getEmail(seq)).thenReturn(email);
//
//        mockMvc.perform(get("/account/email/{seq}", seq))
//                .andExpect(status().isOk())
//                .andExpect(content().string(email));
//    }
//


}
