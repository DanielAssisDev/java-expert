package com.daniel.dev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {
    @NotBlank(message = "Campo requerido")
    @Email(message = "Email inválido")
    private String email;

    public EmailDTO(String email) {
        this.email = email;
    }

    public EmailDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
