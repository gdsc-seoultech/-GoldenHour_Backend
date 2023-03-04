package com.gdsc.goldenhour.messageReport.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "situation")
public class Situation {

    @Id
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private ReportType type;

    @OneToMany(mappedBy = "situation")
    private List<TypeSituation> typeSituationList = new ArrayList<>();

    @OneToMany(mappedBy = "situation")
    private List<DetailSituation> detailSituationList = new ArrayList<>();

    @OneToMany(mappedBy = "situation")
    private List<SituationAndInformation> situationAndInformationList = new ArrayList<>();
}
