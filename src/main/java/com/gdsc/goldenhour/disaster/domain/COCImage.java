package com.gdsc.goldenhour.disaster.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coc_image")
public class COCImage {

    @Id
    private Long id;

    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "disaster_id")
    private Disaster disaster;
}
