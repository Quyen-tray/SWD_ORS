package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.RoleName;
import org.ors.cross.share_kernel.entity.User;
import org.ors.cross.share_kernel.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository dùng chung cho entity User (share kernel): Iam dùng để đăng nhập,
// subsystem moderator_admin dùng để tra cứu và đổi trạng thái tài khoản.
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    // UC-53 - tìm theo từ khoá (email). Bảng users không có cột name nên từ khoá
    // chỉ áp lên email, đúng với dữ liệu thật của schema.
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByKeyword(String keyword);

    // UC-53 - lọc theo vai trò và trạng thái. Truyền null cho tiêu chí không lọc.
    @Query("SELECT u FROM User u "
            + "WHERE (:role IS NULL OR u.role = :role) "
            + "AND (:status IS NULL OR u.status = :status)")
    List<User> findByFilter(RoleName role, UserStatus status);
}
