package com.gdsc.goldenhour.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private String googleId;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ReliefGoods> reliefGoodsList = new ArrayList<>();

    public void addReliefGoods(ReliefGoods reliefGoods) {
        reliefGoodsList.add(reliefGoods);
        reliefGoods.setUser(this);
    }

    public void removeReliefGoods(ReliefGoods reliefGoods) {
        reliefGoodsList.remove(reliefGoods);
        reliefGoods.setUser(null);
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<EmergencyContact> emergencyContactList = new ArrayList<>();

    public void addEmergencyContact(EmergencyContact emergencyContact) {
        emergencyContactList.add(emergencyContact);
        emergencyContact.setUser(this);
    }

    public void removeEmergencyContact(EmergencyContact emergencyContact) {
        emergencyContactList.remove(emergencyContact);
        emergencyContact.setUser(null);
    }

    @Builder
    public User(String googleId) {
        this.googleId = googleId;
    }
}