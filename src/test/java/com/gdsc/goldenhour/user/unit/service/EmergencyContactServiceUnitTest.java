package com.gdsc.goldenhour.user.unit.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.user.domain.EmergencyContact;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.dto.request.EmergencyContactReq;
import com.gdsc.goldenhour.user.repository.EmergencyContactRepository;
import com.gdsc.goldenhour.user.service.EmergencyContactService;
import com.gdsc.goldenhour.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.gdsc.goldenhour.user.dto.response.EmergencyContactRes.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmergencyContactServiceUnitTest {

    @InjectMocks
    private EmergencyContactService emergencyContactService;

    @Mock
    EmergencyContactRepository emergencyContactRepository;

    @Mock
    UserService userService;

    @Test
    public void 비상연락망_조회() {
        // given
        User user = User.builder()
                .googleId("userId")
                .build();
        EmergencyContact emergencyContact_1 = EmergencyContact.builder()
                .name("비상연락망 1 이름")
                .phoneNumber("010-1111-1111")
                .build();
        user.addEmergencyContact(emergencyContact_1);
        EmergencyContact emergencyContact_2 = EmergencyContact.builder()
                .name("비상연락망 2 이름")
                .phoneNumber("010-2222-2222")
                .build();
        user.addEmergencyContact(emergencyContact_2);

        // stub - 동작 지정
        when(userService.readUser("userId")).thenReturn(user);

        // when
        List<EmergencyContactReadRes> emergencyContactReadResList = emergencyContactService.readEmergencyContactList("userId");

        // then
        Assertions.assertThat(emergencyContactReadResList).size().isEqualTo(2);

        Assertions.assertThat(emergencyContactReadResList.get(0).getName()).isEqualTo("비상연락망 1 이름");
        Assertions.assertThat(emergencyContactReadResList.get(0).getPhoneNumber()).isEqualTo("010-1111-1111");

        Assertions.assertThat(emergencyContactReadResList.get(1).getName()).isEqualTo("비상연락망 2 이름");
        Assertions.assertThat(emergencyContactReadResList.get(1).getPhoneNumber()).isEqualTo("010-2222-2222");
    }

    @Test
    public void 비상연락망_저장() {
        // given
        User user = User.builder()
                .googleId("userId")
                .build();

        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름")
                .phoneNumber("010-1111-1111")
                .build();

        // stub - 동작 지정
        when(userService.readUser("userId")).thenReturn(user);

        // when
        EmergencyContactCreateRes emergencyContactCreateRes = emergencyContactService.createEmergencyContact(emergencyContactReq, "userId");

        // then
        Assertions.assertThat(emergencyContactCreateRes.getName()).isEqualTo("비상연락망 이름");
        Assertions.assertThat(emergencyContactCreateRes.getPhoneNumber()).isEqualTo("010-1111-1111");
    }

    @Test
    public void 비상연락망_수정() {
        // given
        Long emergencyContactId = 1L;
        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name("비상연락망 이름")
                .phoneNumber("010-1111-1111")
                .build();
        User user = User.builder()
                .googleId("userId")
                .build();
        user.addEmergencyContact(emergencyContact);

        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-2222-2222")
                .build();

        // stub - 동작 지정
        when(emergencyContactRepository.findById(emergencyContactId)).thenReturn(Optional.of(emergencyContact));
        when(userService.readUser("userId")).thenReturn(user);

        // when
        EmergencyContactUpdateRes emergencyContactUpdateRes = emergencyContactService.updateEmergencyContact(emergencyContactReq, emergencyContactId, "userId");

        // then
        Assertions.assertThat(emergencyContactUpdateRes.getName()).isEqualTo("비상연락망 이름 수정");
        Assertions.assertThat(emergencyContactUpdateRes.getPhoneNumber()).isEqualTo("010-2222-2222");
        Assertions.assertThat(user.getEmergencyContactList().get(0).getName()).isEqualTo("비상연락망 이름 수정");
        Assertions.assertThat(user.getEmergencyContactList().get(0).getPhoneNumber()).isEqualTo("010-2222-2222");
    }

    @Test
    public void 비상연락망_수정_ITEM_NOT_FOUND() {
        // given
        Long not_found_emergencyContactId = 1L;
        User user = User.builder()
                .googleId("userId")
                .build();

        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-2222-2222")
                .build();

        // stub - 동작 지정
        when(emergencyContactRepository.findById(not_found_emergencyContactId)).thenReturn(Optional.empty());
        when(userService.readUser("userId")).thenReturn(user);

        // when & then
        Assertions.assertThatThrownBy(() -> emergencyContactService.updateEmergencyContact(emergencyContactReq, not_found_emergencyContactId, "userId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("존재하지 않는 항목입니다.");
    }

    @Test
    public void 비상연락망_수정_INVALID_USER() {
        // given
        Long emergencyContactId = 1L;
        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name("비상연락망 이름")
                .phoneNumber("010-1111-1111")
                .build();
        User user = User.builder()
                .googleId("userId")
                .build();
        user.addEmergencyContact(emergencyContact);
        User invalidUser = User.builder()
                .googleId("invalidUserId")
                .build();

        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-2222-2222")
                .build();

        // stub - 동작 지정
        when(emergencyContactRepository.findById(emergencyContactId)).thenReturn(Optional.of(emergencyContact));
        when(userService.readUser("invalidUserId")).thenReturn(invalidUser);

        // when & then
        Assertions.assertThatThrownBy(() -> emergencyContactService.updateEmergencyContact(emergencyContactReq, emergencyContactId, "invalidUserId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("올바른 사용자가 아닙니다.");
    }

    @Test
    public void 비상연락망_삭제() {
        // given
        User user = User.builder()
                .googleId("userId")
                .build();
        Long emergencyContact_1_id = 1L;
        EmergencyContact emergencyContact_1 = EmergencyContact.builder()
                .name("비상연락망 1 이름")
                .phoneNumber("010-1111-1111")
                .build();
        user.addEmergencyContact(emergencyContact_1);
        EmergencyContact emergencyContact_2 = EmergencyContact.builder()
                .name("비상연락망 2 이름")
                .phoneNumber("010-2222-2222")
                .build();
        user.addEmergencyContact(emergencyContact_2);

        // stub - 동작 지정
        when(emergencyContactRepository.findById(emergencyContact_1_id)).thenReturn(Optional.of(emergencyContact_1));
        when(userService.readUser("userId")).thenReturn(user);

        // when
        emergencyContactService.deleteEmergencyContact(emergencyContact_1_id, "userId");
        List<EmergencyContactReadRes> emergencyContactReadResList = emergencyContactService.readEmergencyContactList("userId");

        // then
        Assertions.assertThat(emergencyContactReadResList).size().isEqualTo(1);

        Assertions.assertThat(emergencyContactReadResList).doesNotContain(new EmergencyContactReadRes(emergencyContact_1));
        Assertions.assertThat(emergencyContactReadResList.get(0).getName()).isEqualTo("비상연락망 2 이름");
        Assertions.assertThat(emergencyContactReadResList.get(0).getPhoneNumber()).isEqualTo("010-2222-2222");
    }

    @Test
    public void 비상연락망_삭제_ITEM_NOT_FOUND() {
        // given
        Long not_found_emergencyContactId = 1L;
        User user = User.builder()
                .googleId("userId")
                .build();

        // stub - 동작 지정
        when(emergencyContactRepository.findById(not_found_emergencyContactId)).thenReturn(Optional.empty());
        when(userService.readUser("userId")).thenReturn(user);

        // when & then
        Assertions.assertThatThrownBy(() -> emergencyContactService.deleteEmergencyContact(not_found_emergencyContactId, "userId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("존재하지 않는 항목입니다.");
    }

    @Test
    public void 비상연락망_삭제_INVALID_USER() {
        // given
        Long emergencyContactId = 1L;
        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name("비상연락망 이름")
                .phoneNumber("010-1111-1111")
                .build();
        User user = User.builder()
                .googleId("userId")
                .build();
        user.addEmergencyContact(emergencyContact);
        User invalidUser = User.builder()
                .googleId("invalidUserId")
                .build();

        // stub - 동작 지정
        when(emergencyContactRepository.findById(emergencyContactId)).thenReturn(Optional.of(emergencyContact));
        when(userService.readUser("invalidUserId")).thenReturn(invalidUser);

        // when & then
        Assertions.assertThatThrownBy(() -> emergencyContactService.deleteEmergencyContact(emergencyContactId, "invalidUserId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("올바른 사용자가 아닙니다.");
    }
}
