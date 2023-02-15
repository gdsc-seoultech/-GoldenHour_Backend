package com.gdsc.goldenhour.user.dto.response;

import com.gdsc.goldenhour.user.domain.EmergencyContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class EmergencyContactRes {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class EmergencyContactReadRes {
        private Long id;

        private String name;

        private String phoneNumber;

        public EmergencyContactReadRes(EmergencyContact emergencyContact) {
            this.id = emergencyContact.getId();
            this.name = emergencyContact.getName();
            this.phoneNumber = emergencyContact.getPhoneNumber();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class EmergencyContactCreateRes {
        private String name;

        private String phoneNumber;

        public EmergencyContactCreateRes(EmergencyContact emergencyContact) {
            this.name = emergencyContact.getName();
            this.phoneNumber = emergencyContact.getPhoneNumber();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class EmergencyContactUpdateRes {
        private String name;

        private String phoneNumber;

        public EmergencyContactUpdateRes(EmergencyContact emergencyContact) {
            this.name = emergencyContact.getName();
            this.phoneNumber = emergencyContact.getPhoneNumber();
        }
    }

}
