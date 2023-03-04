package com.gdsc.goldenhour.messageReport.domain;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;

public enum ReportType {
    POLICE("112"),
    EMERGENCY("119");

    private final String number;

    ReportType(String number) {
        this.number = number;
    }

    public static ReportType of(String number) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.number.equals(number)) {
                return reportType;
            }
        }

        throw new CustomCommonException(ErrorCode.ITEM_NOT_FOUND);
    }
}
