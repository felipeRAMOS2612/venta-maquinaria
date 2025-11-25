package com.gestionseguridad.venta_maquinaria.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}