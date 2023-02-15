package com.gdsc.goldenhour.disaster.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import com.gdsc.goldenhour.disaster.service.DisasterService;
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
@RequestMapping("/disaster")
public class DisasterController {

    private final DisasterService disasterService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> readDisasterList() {
        return new ResponseEntity<>(ResponseDto.success(disasterService.readDisasterList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> readCOCImageList(@PathVariable Long id) {
        return new ResponseEntity<>(ResponseDto.success(disasterService.readCOCImageList(id)), HttpStatus.OK);
    }

}