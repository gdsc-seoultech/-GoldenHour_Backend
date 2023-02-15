package com.gdsc.goldenhour.disaster.domain;

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
@Table(name = "disaster")
public class Disaster {

    @Id
    private Long id;

    private String name;

    private String imgUrl;

    @OneToMany(mappedBy = "disaster")
    private List<COCImage> cocImageList = new ArrayList<>();
}
