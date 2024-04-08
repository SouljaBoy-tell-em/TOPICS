package com.project.project.user_config;


import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Builder
@Data
@Entity
@Table(name = "Users")
public class User implements Serializable, UserDetails {

    @Column(name = "username")
    @Id
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "user_role")
    @Getter
    private UserRole role;


    public User() {
        this.role = UserRole.ROLE_USER;
    }

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role     =  (role != null) ? role : UserRole.ROLE_USER;
    }

    public User(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role     = (user.getRole() != null) ? role : UserRole.ROLE_USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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