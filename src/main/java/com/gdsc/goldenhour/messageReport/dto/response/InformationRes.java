package com.gdsc.goldenhour.messageReport.dto.response;

import com.gdsc.goldenhour.messageReport.domain.Information;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class InformationRes {

    private Long id;

    private String question;

    private List<String> answerList = new ArrayList<>();

    public InformationRes(Information information) {
        this.id = information.getId();
        this.question = information.getQuestion();
    }
}
