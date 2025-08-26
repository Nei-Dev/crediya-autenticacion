package com.crediya.model.user;

public enum UserRole {
    ADMIN,
    ADVISER,
    CLIENT;
    
    public static UserRole getRol(String rolName) {
        for (UserRole rol : UserRole.values()) {
            if (rol.name().equalsIgnoreCase(rolName)) {
                return rol;
            }
        }
        return null;
    }
}

