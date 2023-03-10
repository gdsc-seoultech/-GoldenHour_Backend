package com.gdsc.goldenhour.guide.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.guide.domain.Guide;
import com.gdsc.goldenhour.guide.dto.response.GuideImageRes;
import com.gdsc.goldenhour.guide.dto.response.GuideRes;
import com.gdsc.goldenhour.guide.repository.GuideImageRepository;
import com.gdsc.goldenhour.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GuideService {

    private final GuideRepository guideRepository;
    private final GuideImageRepository guideImageRepository;

    @Transactional(readOnly = true)
    public List<GuideRes> readGuideList() {
        List<GuideRes> response = new ArrayList<>();

        guideRepository.findAll().forEach(
                guide -> response.add(new GuideRes(guide))
        );

        return response;
    }

    @Transactional(readOnly = true)
    public List<GuideImageRes> readGuideImageList(Long guideId) {
        Guide guide = readGuide(guideId);

        List<GuideImageRes> response = new ArrayList<>();
        guideImageRepository.findAllByGuide(guide).forEach(
                guideImage -> response.add(new GuideImageRes(guideImage))
        );

        return response;
    }

    @Transactional(readOnly = true)
    public List<GuideImageRes> readGuideImageList(String guideName) {
        Guide guide = readGuide(guideName);

        List<GuideImageRes> response = new ArrayList<>();
        guideImageRepository.findAllByGuide(guide).forEach(
                guideImage -> response.add(new GuideImageRes(guideImage))
        );

        return response;
    }

    private Guide readGuide(Long guideId) {
        return guideRepository.findById(guideId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND));
    }

    private Guide readGuide(String guideName) {
        return guideRepository.findByName(guideName)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND));
    }
}
