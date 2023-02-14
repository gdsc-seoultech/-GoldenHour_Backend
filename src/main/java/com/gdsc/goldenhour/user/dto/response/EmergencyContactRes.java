package com.gdsc.goldenhour.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class EmergencyContactRes {

    private Long id;

    private String name;

    private String phoneNumber;
}
