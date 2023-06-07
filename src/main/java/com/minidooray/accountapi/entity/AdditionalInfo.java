package com.minidooray.accountapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "additional_info")
public class AdditionalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "additional_info_seq")
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_seq", referencedColumnName = "account_seq")
    private Account account;


    @Column(name = "email", nullable = false, length = 30, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 13)
    private String phoneNumber;


    public void setAdditionalInfo(String email, String phoneNumber){
        this.email=email;
        this.phoneNumber=phoneNumber;
    }

    public void setAccountByInfo(Account account){
        this.account = account;
    }
}