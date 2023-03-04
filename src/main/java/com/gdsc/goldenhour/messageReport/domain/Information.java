package com.gdsc.goldenhour.messageReport.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "information")
public class Information {

    @Id
    private Long id;

    private String question;

    @OneToMany(mappedBy = "information")
    private List<AnswerInformation> answerInformationList = new ArrayList<>();

    @OneToMany(mappedBy = "information")
    private List<SituationAndInformation> situationAndInformationList = new ArrayList<>();
}
