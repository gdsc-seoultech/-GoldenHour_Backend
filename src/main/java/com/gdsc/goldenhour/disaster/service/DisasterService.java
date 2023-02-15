package com.gdsc.goldenhour.disaster.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.disaster.domain.Disaster;
import com.gdsc.goldenhour.disaster.dto.response.COCImageRes;
import com.gdsc.goldenhour.disaster.dto.response.DisasterRes;
import com.gdsc.goldenhour.disaster.repository.COCImageRepository;
import com.gdsc.goldenhour.disaster.repository.DisasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DisasterService {

    private final DisasterRepository disasterRepository;
    private final COCImageRepository cocImageRepository;

    @Transactional(readOnly = true)
    public List<DisasterRes> readDisasterList() {
        List<DisasterRes> response = new ArrayList<>();

        disasterRepository.findAll().forEach(
                disaster -> response.add(new DisasterRes(disaster))
        );

        return response;
    }

    @Transactional(readOnly = true)
    public List<COCImageRes> readCOCImageList(Long disasterId) {
        Disaster disaster = readDisaster(disasterId);

        List<COCImageRes> response = new ArrayList<>();
        cocImageRepository.findAllByDisaster(disaster).forEach(
                cocImage -> response.add(new COCImageRes(cocImage))
        );

        return response;
    }

    private Disaster readDisaster(Long disasterId) {
        return disasterRepository.findById(disasterId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND));
    }
}
