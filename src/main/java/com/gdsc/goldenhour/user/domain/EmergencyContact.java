package com.gdsc.goldenhour.user.domain;

import com.gdsc.goldenhour.user.dto.request.EmergencyContactReq;
import com.gdsc.goldenhour.user.dto.response.EmergencyContactRes;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "emergency_contact")
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String phoneNumber;

    @Builder
    public EmergencyContact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void setUser(User user) {
        user.getEmergencyContactList().add(this);
        this.user = user;
    }

    public void update(EmergencyContactReq request) {
        name = request.getName();
        phoneNumber = request.getPhoneNumber();
    }

    public EmergencyContactRes toEmergencyContactRes() {
        return EmergencyContactRes.builder()
                .id(id)
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
    }
}
