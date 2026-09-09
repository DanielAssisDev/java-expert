package com.daniel.dev.dto;

import com.daniel.dev.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    private Long id;
    @NotBlank(message = "Campo requerido")
    @Size(min = 3, max = 60, message = "O nome deve ter entre 5 e 60 caracteres")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Campo requerido")
    @Email(message = "Favor digitar um email válido")
    private String email;
    private String phone;
    @PastOrPresent(message = "A data do produto não pode ser futura")
    private Instant birthDate;
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getUsername();
        phone = user.getPhone();
        birthDate = user.getBirthDate();
        user.getRoles().forEach(x -> this.roles.add(new RoleDTO(x)));
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

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}