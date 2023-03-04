package com.gdsc.goldenhour.messageReport.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "situation_and_information")
public class SituationAndInformation {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "situation_id")
    private Situation situation;

    @ManyToOne
    @JoinColumn(name = "information_id")
    private Information information;
}
