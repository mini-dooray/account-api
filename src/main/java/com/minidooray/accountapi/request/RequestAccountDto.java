package com.minidooray.accountapi.request;

import lombok.*;

import javax.persistence.PrePersist;
import javax.validation.constraints.Max;
import java.time.LocalDate;

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
