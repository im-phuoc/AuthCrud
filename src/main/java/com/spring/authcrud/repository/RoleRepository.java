package com.spring.authcrud.repository;

import com.spring.authcrud.models.ERole;
import com.spring.authcrud.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
