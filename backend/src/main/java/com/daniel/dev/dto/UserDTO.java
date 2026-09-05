package com.daniel.dev.dto;

import com.daniel.dev.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private List<String> roles = new ArrayList<>();

    public UserDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getUsername();
        phone = user.getPhone();
        birthDate = user.getBirthDate();
        for(GrantedAuthority grantedAuthority : user.getAuthorities()){
            roles.add(grantedAuthority.getAuthority());
        }
    }

    public UserDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}