package com.trustai.user_service.user.repository;

import com.trustai.user_service.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
    List<User> findByIdIn(List<Long> ids);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);

    List<User> findByAccountStatus(User.AccountStatus accountStatus);
    Page<User> findByAccountStatus(User.AccountStatus accountStatus, Pageable pageable);
}
