package com.nofront.plantinggrassbe.domain;

public class RoleType {
    public static final String ADMIN = "ADMIN";
    public static final String GUEST = "GUEST";
    public static final String MANAGER = "MANAGER";
    public static final String MEMBER = "MEMBER";

    public static void main(String[] args) {
        String user1 = RoleType.ADMIN;
        String user2 = RoleType.GUEST;

        enumRoleType user3 = enumRoleType.MANAGER;
        enumRoleType user4 = enumRoleType.MEMBER;
    }
}

enum enumRoleType {
    ADMIN, GUEST, MANAGER, MEMBER
}
