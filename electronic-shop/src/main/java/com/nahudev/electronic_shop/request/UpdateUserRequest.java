package com.nahudev.electronic_shop.request;

import com.nahudev.electronic_shop.model.Role;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Data
public class UpdateUserRequest {

    private String firstName;

    private String lastName;

    private List<String> roles;

}
