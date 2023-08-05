package com.dev.calendara.apply.domain.enumeration;

public enum ApplyStatus {
    WAIT("미팅 신청 후 대기상태"),
    APPROVE("미팅 신청 승인"),
    REJECT("미팅 신청 반려");

    private final String description;

    ApplyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
