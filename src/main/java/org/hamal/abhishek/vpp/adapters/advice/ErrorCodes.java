package org.hamal.abhishek.vpp.adapters.advice;

import lombok.Getter;

@Getter
public enum ErrorCodes {

    BAD_REQUEST("001", "Bad Request"),
    INTERNAL_ERROR("999", "Something went wrong");

    private final String code;
    private final String description;

    ErrorCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCodeAndDescription() {
        return String.format("%s (%s)", code, description);
    }

}
