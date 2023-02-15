package com.gdsc.goldenhour.guide.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "guide")
public class Guide {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "guide")
    private List<GuideImage> guideImageList = new ArrayList<>();
}
