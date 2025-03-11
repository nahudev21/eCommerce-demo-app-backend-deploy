package com.nahudev.electronic_shop.request;

import com.nahudev.electronic_shop.model.Role;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;

@Data
public class UpdateUserRoleRequest {

    private Role role;

}
