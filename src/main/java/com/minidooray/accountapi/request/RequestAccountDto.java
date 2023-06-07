package com.minidooray.accountapi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class RequestAccountDto {
    private Long accountSeq;
    private String accountId;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime lastAccessDate;
    @Min(1)
    @Max(3)
    private Integer status = 1;
}
