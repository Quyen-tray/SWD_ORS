package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    // AuditLog.user là quan hệ LAZY. JOIN FETCH nạp sẵn user trong cùng 1 câu truy vấn,
    // để AuditLogResponse.from() đọc user.getEmail() được mà không cần transaction bao quanh
    // (tránh LazyInitializationException khi service đọc không mở session).

    // UC-61: xem toàn bộ nhật ký, mới nhất trước.
    @Query("SELECT a FROM AuditLog a JOIN FETCH a.user ORDER BY a.createdAt DESC")
    List<AuditLog> findAllByOrderByCreatedAtDesc();

    // UC-62: lịch sử thao tác TÁC ĐỘNG LÊN một người dùng cụ thể (Activate/Deactivate/Ban),
    // KHÔNG phải thao tác DO người đó thực hiện. a.user là actor (người bấm nút), không phải
    // đối tượng bị tác động - đối tượng bị tác động chỉ được ghi trong description dạng
    // "target=<id>" (xem comment ở AuditLogService.record). Nên phải lọc theo description,
    // không lọc theo a.user.id. Giới hạn thêm actionType để không trùng target của danh mục
    // (cả hai domain đều đánh số target từ 1, "target=5" có thể vừa là user vừa là category).
    @Query("SELECT a FROM AuditLog a JOIN FETCH a.user WHERE a.actionType IN ('ACTIVATE_USER', 'DEACTIVATE_USER', 'BAN_USER') "
            + "AND (a.description = CONCAT('target=', :userId) OR a.description LIKE CONCAT('target=', :userId, ';%')) "
            + "ORDER BY a.createdAt DESC")
    List<AuditLog> findUserActionsByTargetUserId(@Param("userId") Integer userId);
}
