package com.gdsc.goldenhour.user.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import com.gdsc.goldenhour.user.domain.EmergencyContact;
import com.gdsc.goldenhour.user.dto.request.EmergencyContactReq;
import com.gdsc.goldenhour.user.service.EmergencyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/emergency_contact")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> readEmergencyContactList(@AuthenticationPrincipal String userId) {
        return new ResponseEntity<>(ResponseDto.success(emergencyContactService.readEmergencyContactList(userId)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createEmergencyContact(@Valid @RequestBody EmergencyContactReq emergencyContactReq, @AuthenticationPrincipal String userId) {
        EmergencyContact createdEmergencyContact = emergencyContactService.createEmergencyContact(emergencyContactReq, userId);
        return new ResponseEntity<>(ResponseDto.success(createdEmergencyContact.toEmergencyContactRes()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> updateEmergencyContact(@Valid @RequestBody EmergencyContactReq emergencyContactReq, @PathVariable Long id, @AuthenticationPrincipal String userId) {
        EmergencyContact updatedEmergencyContact = emergencyContactService.updateEmergencyContact(emergencyContactReq, id, userId);
        return new ResponseEntity<>(ResponseDto.success(updatedEmergencyContact.toEmergencyContactRes()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> deleteEmergencyContact(@PathVariable Long id, @AuthenticationPrincipal String userId) {
        emergencyContactService.deleteEmergencyContact(id, userId);
        return new ResponseEntity<>(ResponseDto.success("비상 연락망 삭제 완료"), HttpStatus.OK);
    }
}
