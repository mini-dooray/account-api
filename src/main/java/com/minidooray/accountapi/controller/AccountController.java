package com.minidooray.accountapi.controller;


import com.minidooray.accountapi.entity.Account;
import com.minidooray.accountapi.request.RequestAccountDto;
import com.minidooray.accountapi.response.ResponseAccountDto;
import com.minidooray.accountapi.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account/{seq}")
    public ResponseAccountDto getAccount(@PathVariable Long seq) {
        return accountService.getAccount(seq);
    }

    @PostMapping("/account/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> register(@RequestBody RequestAccountDto requestAccountDto){
        accountService.register(requestAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping(value = "/account/delete/{seq}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseAccountDto deleteAccount(@PathVariable Long seq) {
        return accountService.deleteAccount(seq);
    }

    @PutMapping("/account/update/{seq}")
    public ResponseAccountDto updateAccount(@PathVariable Long seq, @RequestBody RequestAccountDto requestAccountDto) {
        return accountService.updateAccount(seq, requestAccountDto);
    }

    @GetMapping("/account/user/{seq}")
    public String getAccountId(@PathVariable Long seq){
        return accountService.getAccountId(seq);
    }

    @GetMapping("/account/email/{seq}")
    public String getEmail(@PathVariable Long seq){
        return accountService.getEmail(seq);
    }

    @GetMapping("/account/pwd/{seq}")
    public String getPassword(@PathVariable Long seq){
        return accountService.getPassword(seq);
    }

    @GetMapping("/account/{email}/found")
    public ResponseAccountDto getAccountByEmail(@PathVariable String email) {
        return accountService.getAccountByEmail(email);
    }

    @GetMapping("/account/find/email/{email}")
    public Boolean existsEmail(@PathVariable String email){
        return accountService.existEmail(email);
    }

    @GetMapping("/account/find/id/{id}")
    public Boolean existsId(@PathVariable String id){
        return accountService.existId(id);
    }

    @PostMapping("/account/login")
    public ResponseEntity<String> login(@RequestBody RequestAccountDto requestDto) {
        String accountId = requestDto.getAccountId();
        String password = requestDto.getPassword();

        boolean login = accountService.existLoginAccount(accountId, password);

        if (login) {
            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

}