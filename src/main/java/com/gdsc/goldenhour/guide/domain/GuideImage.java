package com.gdsc.goldenhour.guide.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "guide_image")
public class GuideImage {

    @Id
    private Long id;

    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;
}
