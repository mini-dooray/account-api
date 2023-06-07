package com.minidooray.accountapi.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResponseAccountDto {
    private Long accountSeq;
    private String accountId;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate lastAccessDate;
    private Integer status;
}

