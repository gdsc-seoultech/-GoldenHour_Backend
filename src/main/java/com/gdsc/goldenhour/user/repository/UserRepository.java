package com.gdsc.goldenhour.user.repository;

import com.gdsc.goldenhour.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
