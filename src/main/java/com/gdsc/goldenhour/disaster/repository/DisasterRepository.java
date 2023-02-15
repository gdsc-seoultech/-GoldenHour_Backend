package com.gdsc.goldenhour.disaster.repository;

import com.gdsc.goldenhour.disaster.domain.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisasterRepository extends JpaRepository<Disaster, Long> {
}
