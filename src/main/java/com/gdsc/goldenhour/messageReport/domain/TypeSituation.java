package com.gdsc.goldenhour.messageReport.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "type_situation")
public class TypeSituation {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "situation_id")
    private Situation situation;

}
