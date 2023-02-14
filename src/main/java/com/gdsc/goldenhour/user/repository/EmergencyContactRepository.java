package com.gdsc.goldenhour.user.repository;

import com.gdsc.goldenhour.user.domain.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
}
