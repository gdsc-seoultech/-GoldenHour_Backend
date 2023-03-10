package com.gdsc.goldenhour.guide.repository;

import com.gdsc.goldenhour.guide.domain.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    Optional<Guide> findByName(String name);
}
