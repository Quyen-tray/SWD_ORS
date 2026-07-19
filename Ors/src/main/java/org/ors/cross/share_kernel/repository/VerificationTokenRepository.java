package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Dung boi: Auth (dung chung).
// Them cac query method rieng cua phan minh vao day (vd findByCompanyId, existsBy...).
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);
}
