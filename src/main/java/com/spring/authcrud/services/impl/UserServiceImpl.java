package com.spring.authcrud.services.impl;

import com.spring.authcrud.models.ERole;
import com.spring.authcrud.models.Role;
import com.spring.authcrud.models.User;
import com.spring.authcrud.payload.request.UpdateRoleRequest;
import com.spring.authcrud.payload.response.PagedResponse;
import com.spring.authcrud.payload.response.UserResponse;
import com.spring.authcrud.repository.RoleRepository;
import com.spring.authcrud.repository.UserRepository;
import com.spring.authcrud.security.services.UserDetailsImpl;
import com.spring.authcrud.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @Override
    public PagedResponse<UserResponse> getAllUsers(int size, int page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> users = userRepository.findAll(pageable);
        Set<UserResponse> responses =  users.getContent().stream().map(
                user -> new UserResponse(
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().stream().map(
                                role -> role.getName().name()
                        ).collect(Collectors.toSet())
                )
        ).collect(Collectors.toSet());
        return new PagedResponse<>(responses,users.getNumber(),users.getSize(),users.getTotalPages(),users.getTotalElements(),users.isLast());

    }

    @Override
    public UserResponse getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        return new UserResponse(user.getUsername(),user.getEmail(),roles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void updateRole(String username, UpdateRoleRequest updateRoleRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        Set<String> roleNames = updateRoleRequest.getRoles();
        if (roleNames==null || roleNames.isEmpty()) {
            throw new RuntimeException("Roles cannot be empty");
        }
        Set<Role> updateRoles = new HashSet<>();
        for (String roleName : roleNames) {
            ERole eRole;
            try {
                eRole = ERole.valueOf(roleName);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role name "+roleName);
            }
            Role role = roleRepository.findByName(eRole).orElseThrow(
                    () -> new RuntimeException("Invalid role name "+eRole)
            );
            updateRoles.add(role);
        }
        user.setRoles(updateRoles);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        userRepository.delete(user);
    }
}
