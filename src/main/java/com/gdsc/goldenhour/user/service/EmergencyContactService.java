package com.gdsc.goldenhour.user.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.user.domain.EmergencyContact;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.dto.request.EmergencyContactReq;
import com.gdsc.goldenhour.user.dto.response.EmergencyContactRes;
import com.gdsc.goldenhour.user.repository.EmergencyContactRepository;
import com.gdsc.goldenhour.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmergencyContactService {

    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    @Transactional(readOnly = true)
    public List<EmergencyContactRes> readEmergencyContactList(String userId) {
        User user = readUser(userId);

        List<EmergencyContactRes> response = new ArrayList<>();
        user.getEmergencyContactList().forEach(
                emergencyContact -> response.add(emergencyContact.toEmergencyContactRes())
        );
        return response;
    }

    @Transactional
    public EmergencyContact createEmergencyContact(EmergencyContactReq request, String userId) {
        User user = readUser(userId);

        EmergencyContact emergencyContact = request.toEmergencyContact();
        emergencyContact.setUser(user);

        return emergencyContact;
    }

    @Transactional
    public EmergencyContact updateEmergencyContact(EmergencyContactReq request, Long emergencyContactId, String userId) {
        EmergencyContact emergencyContact = readEmergencyContact(emergencyContactId);
        validateUser(userId, emergencyContact);

        emergencyContact.update(request);

        return emergencyContact;
    }

    @Transactional
    public void deleteEmergencyContact(Long emergencyContactId, String userId) {
        EmergencyContact emergencyContact = readEmergencyContact(emergencyContactId);
        validateUser(userId, emergencyContact);

        emergencyContactRepository.deleteById(emergencyContactId);
    }

    private EmergencyContact readEmergencyContact(Long emergencyContactId) {
        return emergencyContactRepository.findById(emergencyContactId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND));
    }

    private User readUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUser(String userId, EmergencyContact emergencyContact) {
        User user = readUser(userId);
        if (!emergencyContact.getUser().equals(user)) {
            throw new CustomCommonException(ErrorCode.INVALID_USER);
        }
    }
}
