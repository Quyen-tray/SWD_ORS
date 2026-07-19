package org.ors.cross.Iam.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.entity.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Bọc entity User cho Spring Security.
// Lưu ý khác biệt so với bản chép từ project cũ: khoá chính users.id là INT nên id là
// Integer, không phải Long.
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public Integer getId() {
        return user.getId();
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

    // Chỉ tài khoản ACTIVE mới đăng nhập được. INACTIVE (bị Admin tạm khoá), BANNED
    // (bị Admin cấm), EMAIL_PENDING (chưa xác thực email) và LOCKED đều bị chặn ở đây.
    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE == user.getStatus();
    }

    public UserStatus getStatus() {
        return user.getStatus();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
