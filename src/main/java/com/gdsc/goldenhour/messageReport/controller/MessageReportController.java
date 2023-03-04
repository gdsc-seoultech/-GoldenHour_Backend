package com.gdsc.goldenhour.messageReport.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import com.gdsc.goldenhour.messageReport.domain.ReportType;
import com.gdsc.goldenhour.messageReport.service.MessageReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageReportController {

    private final MessageReportService messageReportService;

    @GetMapping("/{type}")
    public ResponseEntity<ResponseDto<?>> readSituationList(@PathVariable String type) {
        return new ResponseEntity<>(ResponseDto.success(messageReportService.readSituationList(ReportType.of(type))), HttpStatus.OK);
    }

    @GetMapping("/{id}/type_situation")
    public ResponseEntity<ResponseDto<?>> readTypeSituationList(@PathVariable Long id) {
        return new ResponseEntity<>(ResponseDto.success(messageReportService.readTypeSituationList(id)), HttpStatus.OK);
    }

    @GetMapping("/{id}/detail_situation")
    public ResponseEntity<ResponseDto<?>> readDetailSituationList(@PathVariable Long id) {
        return new ResponseEntity<>(ResponseDto.success(messageReportService.readDetailSituationList(id)), HttpStatus.OK);
    }

    @GetMapping("/{id}/information")
    public ResponseEntity<ResponseDto<?>> readInformationList(@PathVariable Long id) {
        return new ResponseEntity<>(ResponseDto.success(messageReportService.readInformationList(id)), HttpStatus.OK);
    }
}
