package com.nahudev.electronic_shop.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Data
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private ZonedDateTime createdAt;

    private List<OrderDTO> orders;

    private CartDTO cart;

    private Collection<RoleDTO> roles = new HashSet<>();

}
