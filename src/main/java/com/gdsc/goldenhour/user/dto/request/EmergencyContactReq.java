package com.gdsc.goldenhour.user.dto.request;

import com.gdsc.goldenhour.user.domain.EmergencyContact;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
public class EmergencyContactReq {

    @NotBlank(message = "name : 이름을 입력해주세요.")
    private String name;

    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "phoneNumber : 10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;

    @Builder
    public EmergencyContactReq(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public EmergencyContact toEmergencyContact() {
        return EmergencyContact.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
    }
}
