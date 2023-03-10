package com.gdsc.goldenhour.disaster.repository;

import com.gdsc.goldenhour.disaster.domain.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisasterRepository extends JpaRepository<Disaster, Long> {
    Optional<Disaster> findByName(String name);
}
