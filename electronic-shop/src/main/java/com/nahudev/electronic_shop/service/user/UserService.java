package com.nahudev.electronic_shop.service.user;

import com.nahudev.electronic_shop.dto.RoleDTO;
import com.nahudev.electronic_shop.dto.UserDTO;
import com.nahudev.electronic_shop.exceptions.AlreadyExistsException;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Role;
import com.nahudev.electronic_shop.model.User;
import com.nahudev.electronic_shop.repository.IRoleRepository;
import com.nahudev.electronic_shop.repository.IUserRepository;
import com.nahudev.electronic_shop.request.CreateUserRequest;
import com.nahudev.electronic_shop.request.UpdateUserRequest;
import com.nahudev.electronic_shop.request.UpdateUserRoleRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

    private final IUserRepository userRepository;

    private final IRoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()  ->
                new ResourceNotFoundException("User not found!"));
    }

    @Override
    public List<User> getListUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));


                    // Aquí creamos una colección de roles desde la base de datos para evitar crear nuevos roles
                    Set<Role> roles = new HashSet<>();
                    for (Role role : req.getRoles()) {
                        // Verificamos si el rol ya existe en la base de datos
                        Role existingRole = roleRepository.findByName(role.getName());

                        roles.add(existingRole);
                    }


                    // Asignar los roles al usuario
                    user.setRoles(roles);

                    // Guardar el usuario en la base de datos
                    return userRepository.save(user);

                }).orElseThrow(() -> new AlreadyExistsException("The user already exists with email! " + request.getEmail()));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {


        // Obtenemos los roles de la base de datos usando los nombres proporcionados en la solicitud.
        List<Role> roles = roleRepository.findByNameIn(request.getRoles());

        // Verificamos si se encontraron roles válidos
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("No valid roles found for the given names!");
        }

        return userRepository.findById(userId).map(existingUser -> {
            // Actualizamos la información del usuario (otros campos)
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());

            // Actualizamos los roles sin crear nuevos
            existingUser.setRoles(new HashSet<>(roles)); // Establecemos los roles como un conjunto (evitar duplicados)

            // Guardamos los cambios en la base de datos
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("user not found!");
        });
    }

    @Override
    public Role createRole(Role request) {
        return Optional.of(request)
                .filter(role -> !roleRepository.existsByName(role.getName()))
                .map(req -> {
                    Role role = new Role();
                    role.setName(req.getName());
                    return roleRepository.save(role);
                }).orElseThrow(() -> new AlreadyExistsException("The role already exists with name! " + request.getName()));
    }

    @Override
    public Role updateUserRole(UpdateUserRoleRequest request, Long id) {

        Role role = roleRepository.findById(id).orElse(null);
        Role newRole = new Role();
        newRole.setId(role.getId());
        newRole.setName(request.getRole().getName());
        newRole.setUsers(role.getUsers());

        return roleRepository.save(newRole);

    }

    @Override
    public UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> convertListToDto(List<User> users) {
        return users.stream().map(this::convertToDto).toList();
    }

    @Override
    public RoleDTO convertRoleToDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

}
