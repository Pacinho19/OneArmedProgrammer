package pl.pacinho.onearmedprogrammer.model.dto;

import org.springframework.security.core.GrantedAuthority;
import pl.pacinho.onearmedprogrammer.model.entity.Account;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AccountDetailsDto implements org.springframework.security.core.userdetails.UserDetails {

    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public AccountDetailsDto(Account account) {
        this.userName = account.getName();
        this.password = account.getPassword();
        this.active = true;
        this.authorities = Collections.emptyList();
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
        return userName;
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
        return active;
    }
}
