package com.unitral.microdoc.repository;

import com.unitral.microdoc.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthenticationRepo extends JpaRepository<UserAuthentication,Integer> {
   UserAuthentication findByUsername(String username);
}
