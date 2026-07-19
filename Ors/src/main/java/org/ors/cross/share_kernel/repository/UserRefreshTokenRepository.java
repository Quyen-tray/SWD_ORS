package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Dung boi: Auth (dung chung).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Integer> {
    Optional<UserRefreshToken> findByToken(String token);
    void deleteByUser(User user);
    boolean existsByToken(String token);
}
