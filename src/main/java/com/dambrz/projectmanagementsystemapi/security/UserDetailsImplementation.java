package com.dambrz.projectmanagementsystemapi.security;

import com.dambrz.projectmanagementsystemapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImplementation implements UserDetails {

    private final Long id;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private final String password;

    public UserDetailsImplementation(Long id, String username, Collection<? extends GrantedAuthority> authorities, String password) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.password = password;
    }

    public static UserDetailsImplementation build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImplementation(user.getId(), user.getUsername(), authorities, user.getPassword());
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
