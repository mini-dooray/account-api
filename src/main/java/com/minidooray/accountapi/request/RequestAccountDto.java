package com.minidooray.accountapi.request;

import lombok.*;

import javax.persistence.PrePersist;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class RequestAccountDto {
    private String accountId;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate lastAccessDate;
    @Max(3)
    private Integer status = 1;


    @PrePersist
    public void onAccess(){
        lastAccessDate= LocalDate.now();
    }
}
