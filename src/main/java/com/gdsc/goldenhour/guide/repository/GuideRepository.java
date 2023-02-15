package com.gdsc.goldenhour.guide.repository;

import com.gdsc.goldenhour.guide.domain.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<Guide, Long> {
}
