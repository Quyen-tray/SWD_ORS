package org.ors.cross.Iam.auth.repositories;

import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Integer> {
    Optional<UserRefreshToken> findByToken(String token);
    void deleteByUser(User user);
    boolean existsByToken(String token);
}
