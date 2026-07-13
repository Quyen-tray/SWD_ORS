package org.ors.subsystem.administration.user_management.lookup;

import org.ors.cross.share_kernel.entity.RoleName;
import org.ors.cross.share_kernel.entity.UserStatus;
import org.ors.cross.share_kernel.exception.BadRequestException;

// Tiêu chí tra cứu người dùng của UC-53 (xem, tìm theo từ khoá, lọc theo vai trò và trạng thái).
// Gom 3 tham số rời thành một object để chiến lược nào cũng nhận đúng một kiểu đầu vào.
//
// Đây là ranh giới giữa thế giới bên ngoài và mô hình bên trong: client gửi lên chuỗi tự do
// (?role=RECRUITER), nên chuỗi được đổi sang enum NGAY TẠI ĐÂY. Vào sâu hơn thì không còn
// chuỗi trạng thái/vai trò nào nữa. Client gửi giá trị không hợp lệ thì bị từ chối bằng
// HTTP 400 kèm danh sách giá trị đúng, thay vì lặng lẽ trả về danh sách rỗng.
public record UserCriteria(String keyword, RoleName role, UserStatus status) {

    public static UserCriteria of(String keyword, String role, String status) {
        return new UserCriteria(
                trimToNull(keyword),
                parse(RoleName.class, trimToNull(role), "vai trò"),
                parse(UserStatus.class, trimToNull(status), "trạng thái"));
    }

    public boolean hasKeyword() {
        return keyword != null;
    }

    public boolean hasRoleOrStatus() {
        return role != null || status != null;
    }

    private static <E extends Enum<E>> E parse(Class<E> type, String value, String label) {
        if (value == null) {
            return null;
        }
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Giá trị " + label + " không hợp lệ: " + value);
        }
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
