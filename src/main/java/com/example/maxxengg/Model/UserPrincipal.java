package com.example.maxxengg.Model;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user) {
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Iterator<Role> iterator = this.user.getRoles().iterator();
        String currUserRole="USER";
        if(iterator.hasNext()){
            currUserRole=iterator.next().getName();
        }
        log.info("currUserRole:"+currUserRole);

        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+currUserRole.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
