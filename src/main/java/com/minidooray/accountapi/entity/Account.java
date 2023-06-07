package com.minidooray.accountapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "account_seq",nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seq;

    @Column(name = "id",nullable = false, length = 12)
    private String id;

    @Column(name = "password",nullable = false,length = 60)
    private String password;

    @Column(name = "name",nullable = false, length = 10)
    private String name;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private AccountStatus accountStatus;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AdditionalInfo additionalInfo;
}

