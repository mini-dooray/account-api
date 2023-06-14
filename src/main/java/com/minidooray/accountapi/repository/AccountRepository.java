package com.minidooray.accountapi.repository;

import com.minidooray.accountapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom{
    List<Account> findAll();
    Optional<Account> findById(Long id);

}

