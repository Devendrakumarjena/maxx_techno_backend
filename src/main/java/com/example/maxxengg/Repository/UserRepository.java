package com.example.maxxengg.Repository;

import com.example.maxxengg.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User ,Long> {
    User findByEmail(String email);
}
