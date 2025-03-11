package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.dto.RoleDTO;
import com.nahudev.electronic_shop.dto.UserDTO;
import com.nahudev.electronic_shop.exceptions.AlreadyExistsException;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Role;
import com.nahudev.electronic_shop.model.User;
import com.nahudev.electronic_shop.request.CreateUserRequest;
import com.nahudev.electronic_shop.request.UpdateUserRequest;
import com.nahudev.electronic_shop.request.UpdateUserRoleRequest;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDTO userDTO = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success!", userDTO));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getListUsers() {
        try {
            List<User> users = userService.getListUsers();
            List<UserDTO> usersDto = userService.convertListToDto(users);
            return ResponseEntity.ok(new ApiResponse("Success!", usersDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDTO userDTO = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create user success!", userDTO));
        } catch ( AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request,
                                                  @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            UserDTO userDTO = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update user success!", userDTO));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Deleted user success!", null));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/role/add")
    public ResponseEntity<ApiResponse> createRole(@RequestBody Role request) {
        try {
            Role role = userService.createRole(request);
            RoleDTO roleDTO = userService.convertRoleToDto(role);
            return ResponseEntity.ok(new ApiResponse("Create role success!", roleDTO));
        } catch ( AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{roleId}/update-role")
    public ResponseEntity<ApiResponse> updateUserRole(@RequestBody UpdateUserRoleRequest request,
                                                  @PathVariable Long roleId) {
        try {
            Role role = userService.updateUserRole(request, roleId);
            RoleDTO roleDTO = userService.convertRoleToDto(role);
            return ResponseEntity.ok(new ApiResponse("Update role success!", roleDTO));
        } catch ( ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
