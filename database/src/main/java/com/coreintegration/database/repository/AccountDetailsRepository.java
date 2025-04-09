package com.coreintegration.database.repository;

import com.coreintegration.database.entity.AccountDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetailsEntity, String> {
}
