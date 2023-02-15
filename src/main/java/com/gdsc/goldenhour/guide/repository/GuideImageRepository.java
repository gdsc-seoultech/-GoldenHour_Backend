package com.gdsc.goldenhour.guide.repository;

import com.gdsc.goldenhour.guide.domain.Guide;
import com.gdsc.goldenhour.guide.domain.GuideImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideImageRepository extends JpaRepository<GuideImage, Long> {

    List<GuideImage> findAllByGuide(Guide guide);
}
