package com.nahudev.electronic_shop.request;

import com.nahudev.electronic_shop.model.Role;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class CreateUserRequest {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Collection<Role> roles = new HashSet<>();

}
