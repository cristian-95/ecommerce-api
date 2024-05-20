package com.commerce.api.model;

public enum UserRole {
    ADMIN("admin"),
    MANAGER("manager"),
    USER("user");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
