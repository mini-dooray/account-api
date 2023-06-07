package com.minidooray.accountapi.repository;

import com.minidooray.accountapi.entity.AdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdditionalInfoRepository extends JpaRepository<AdditionalInfo,String> {

    Optional<AdditionalInfo> findByEmail(String email);

}
