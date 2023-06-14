package com.minidooray.accountapi.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_status")
public class AccountStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_status_seq")
    private Long seq;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_seq", referencedColumnName = "account_seq")
    private Account account;

    @Column(name = "last_access_date")
    private LocalDate accessDate;

    @PrePersist
    protected void onCreate() {
        accessDate = LocalDate.now();
    }


    @Column(name = "status", nullable = false, columnDefinition = "int default 1")
    @Max(3)
    private int status;

    public void setAccountStatus(LocalDate accessDate, int status){
        this.status=status;
        this.accessDate = accessDate;

//        if(account!=null) {
//            this.account.getAccountStatus().setAccountStatus(accessDate, status);
//        }
//        else{
//            account = new Account();
//            account.getAccountStatus().setAccountStatus(accessDate, status);
//
//        }
    }

    public void saveStatus(int status){
        this.status=status;
    }

    public void saveAccessDate(LocalDate localDate){
        this.accessDate = localDate;
    }

    public void setAccountByStatus(Account account){
        this.account = account;
    }

}
