package com.example.maxxengg.Repository;

import com.example.maxxengg.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Custom query to find a role by its name
    Role findByName(String name);
}
