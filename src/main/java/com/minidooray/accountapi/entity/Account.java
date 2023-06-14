package com.minidooray.accountapi.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_seq")
    private Long seq;

    @Column(name = "id",nullable = false, length = 40)
    private String id;

    @Column(name = "password",nullable = false,length = 60)
    private String password;

    @Column(name = "name",nullable = false, length = 10)
    private String name;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private AccountStatus accountStatus;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AdditionalInfo additionalInfo;

    @Builder
    public void createAccount(String  id, String password, String name,String email, String phoneNumber){
        this.id=id;
        this.password=password;
        this.name=name;
        additionalInfo.setAdditionalInfo(email,phoneNumber);
    }
    @Builder
    public void setAccount(String id, String password, String name){
        this.id=id;
        this.password=password;
        this.name=name;
    }



    public void saveInfoAndStatus(AccountStatus accountStatus, AdditionalInfo additionalInfo){
        this.accountStatus=accountStatus;
        this.additionalInfo=additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo){
        this.additionalInfo=additionalInfo;
    }
    public void setAccountStatus(AccountStatus accountStatus){
        this.accountStatus=accountStatus;
    }
}

