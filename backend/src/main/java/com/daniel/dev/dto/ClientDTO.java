package com.daniel.dev.dto;

import com.daniel.dev.entities.User;

public class ClientDTO {
    private Long id;
    private String name;

    public ClientDTO() {
    }

    public ClientDTO(User user) {
        id = id;
        name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
