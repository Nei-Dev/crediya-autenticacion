package com.crediya.model.usuario;

public enum RolUsuario {
    ADMIN,
    ASESOR,
    CLIENTE;
    
    public static RolUsuario getRol(String rolName) {
        for (RolUsuario rol : RolUsuario.values()) {
            if (rol.name().equalsIgnoreCase(rolName)) {
                return rol;
            }
        }
        return null;
    }
}

