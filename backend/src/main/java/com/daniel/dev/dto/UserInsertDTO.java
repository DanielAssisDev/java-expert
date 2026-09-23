package com.daniel.dev.dto;

import com.daniel.dev.entities.User;
import com.daniel.dev.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    @NotBlank(message = "Campo requerido")
    @Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
    private String password;

    public UserInsertDTO(User user) {
        super(user);
    }

    public UserInsertDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
