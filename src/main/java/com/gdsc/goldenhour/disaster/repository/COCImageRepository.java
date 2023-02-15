package com.gdsc.goldenhour.disaster.repository;

import com.gdsc.goldenhour.disaster.domain.COCImage;
import com.gdsc.goldenhour.disaster.domain.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface COCImageRepository extends JpaRepository<COCImage, Long> {

    List<COCImage> findAllByDisaster(Disaster disaster);
}
