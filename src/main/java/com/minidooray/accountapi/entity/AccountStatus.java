package com.minidooray.accountapi.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
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

    @Builder
    public void setAccountStatus(LocalDate accessDate){
        this.accessDate=accessDate;
    }

    public void setAccountByStatus(Account account){
        this.account = account;
    }

}
