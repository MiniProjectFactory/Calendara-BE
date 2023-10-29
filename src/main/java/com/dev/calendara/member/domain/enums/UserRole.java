package com.dev.calendara.member.domain.enums;

public enum UserRole {
    MEMBER("MEMBER");
    private final String role;
    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
