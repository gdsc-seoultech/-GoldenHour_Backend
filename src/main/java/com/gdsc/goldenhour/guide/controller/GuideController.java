package com.gdsc.goldenhour.guide.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import com.gdsc.goldenhour.guide.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/guide")
public class GuideController {

    private final GuideService guideService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> readGuideList() {
        return new ResponseEntity<>(ResponseDto.success(guideService.readGuideList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> readGuideImageList(@PathVariable Long id) {
        return new ResponseEntity<>(ResponseDto.success(guideService.readGuideImageList(id)), HttpStatus.OK);
    }

}
