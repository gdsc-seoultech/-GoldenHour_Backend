package com.gdsc.goldenhour.user.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.user.domain.EmergencyContact;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.dto.request.EmergencyContactReq;
import com.gdsc.goldenhour.user.repository.EmergencyContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.gdsc.goldenhour.user.dto.response.EmergencyContactRes.*;

@RequiredArgsConstructor
@Service
public class EmergencyContactService {

    private final UserService userService;
    private final EmergencyContactRepository emergencyContactRepository;

    @Transactional(readOnly = true)
    public List<EmergencyContactReadRes> readEmergencyContactList(String userId) {
        User user = userService.readUser(userId);

        List<EmergencyContactReadRes> response = new ArrayList<>();
        user.getEmergencyContactList().forEach(
                emergencyContact -> response.add(new EmergencyContactReadRes(emergencyContact))
        );
        return response;
    }

    @Transactional
    public EmergencyContactCreateRes createEmergencyContact(EmergencyContactReq request, String userId) {
        User user = userService.readUser(userId);

        EmergencyContact emergencyContact = request.toEmergencyContact();
        user.addEmergencyContact(emergencyContact);

        emergencyContactRepository.save(emergencyContact);

        return new EmergencyContactCreateRes(emergencyContact);
    }

    @Transactional
    public EmergencyContactUpdateRes updateEmergencyContact(EmergencyContactReq request, Long emergencyContactId, String userId) {
        User user = userService.readUser(userId);
        EmergencyContact emergencyContact = readEmergencyContact(emergencyContactId);

        validateUser(user, emergencyContact);

        emergencyContact.update(request);

        return new EmergencyContactUpdateRes(emergencyContact);
    }

    @Transactional
    public void deleteEmergencyContact(Long emergencyContactId, String userId) {
        User user = userService.readUser(userId);
        EmergencyContact emergencyContact = readEmergencyContact(emergencyContactId);

        validateUser(user, emergencyContact);

        user.removeEmergencyContact(emergencyContact);

        emergencyContactRepository.deleteById(emergencyContactId);
    }

    private EmergencyContact readEmergencyContact(Long emergencyContactId) {
        return emergencyContactRepository.findById(emergencyContactId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND));
    }

    private void validateUser(User user, EmergencyContact emergencyContact) {
        if (!emergencyContact.getUser().equals(user)) {
            throw new CustomCommonException(ErrorCode.INVALID_USER);
        }
    }
}
