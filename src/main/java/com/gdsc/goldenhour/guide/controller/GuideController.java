package com.gdsc.goldenhour.guide.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import com.gdsc.goldenhour.guide.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/")
    public ResponseEntity<ResponseDto<?>> readGuideImageList(@RequestParam String name) {
        return new ResponseEntity<>(ResponseDto.success(guideService.readGuideImageList(name)), HttpStatus.OK);
    }
}
