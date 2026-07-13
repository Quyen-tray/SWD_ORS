package org.ors.subsystem.administration.user_management.lookup;

// Tiêu chí tra cứu người dùng của UC-53 (xem, tìm theo từ khoá, lọc theo vai trò và trạng thái).
// Gom 3 tham số rời thành một object để chiến lược nào cũng nhận đúng một kiểu đầu vào.
public record UserCriteria(String keyword, String role, String status) {

    public static UserCriteria of(String keyword, String role, String status) {
        return new UserCriteria(trimToNull(keyword), trimToNull(role), trimToNull(status));
    }

    public boolean hasKeyword() {
        return keyword != null;
    }

    public boolean hasRoleOrStatus() {
        return role != null || status != null;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
