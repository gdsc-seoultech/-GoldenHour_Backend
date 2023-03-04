package com.gdsc.goldenhour.messageReport.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "answer_information")
public class AnswerInformation {

    @Id
    private Long id;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "information_id")
    private Information information;
}
