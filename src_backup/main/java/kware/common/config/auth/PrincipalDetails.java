package kware.common.config.auth;


import kware.apps.manager.cetus.enumstatus.UserStatus;
import kware.common.config.auth.dto.SessionUserInfo;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(of = {"user"})
public class PrincipalDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private SessionUserInfo user;
    private boolean accountNotExpired;

    public PrincipalDetails(SessionUserInfo user) {
        this.user = user;
    }

    public SessionUserInfo getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(()-> user.getRole());
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return "Y".equals(user.getUseAt());
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.APPROVED.toString().equals(user.getStatus());
    }

}
