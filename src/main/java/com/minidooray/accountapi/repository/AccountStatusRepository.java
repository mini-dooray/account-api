package com.minidooray.accountapi.repository;

import com.minidooray.accountapi.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountStatusRepository extends JpaRepository<AccountStatus,Long> {
}
