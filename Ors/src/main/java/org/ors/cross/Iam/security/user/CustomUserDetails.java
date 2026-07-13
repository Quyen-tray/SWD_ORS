package org.ors.cross.Iam.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ors.cross.share_kernel.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Bọc entity User cho Spring Security.
// Lưu ý khác biệt so với bản chép từ project cũ: trong ORS, users.role và users.status
// là cột NVARCHAR (xem ors.sql), nên getRole()/getStatus() trả về String chứ không phải
// enum - không gọi .name() được. Khoá chính users.id là INT nên id là Integer, không phải Long.
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Chỉ tài khoản ACTIVE mới đăng nhập được. INACTIVE (bị Admin tạm khoá), BANNED
    // (bị Admin cấm), EMAIL_PENDING (chưa xác thực email) và LOCKED đều bị chặn ở đây.
    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(user.getStatus());
    }

    public Integer getId() {
        return user.getId();
    }

    public String getStatus() {
        return user.getStatus();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
