package com.daniel.dev.dto;

import com.daniel.dev.entities.User;
import com.daniel.dev.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{
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
