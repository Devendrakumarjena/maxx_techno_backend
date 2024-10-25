package com.example.maxxengg.Service;

import com.example.maxxengg.Model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User saveUser(User user);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    Optional<User> findByEmail(String email);
}
