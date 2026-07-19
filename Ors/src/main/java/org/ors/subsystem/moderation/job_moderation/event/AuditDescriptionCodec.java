package org.ors.subsystem.moderation.job_moderation.event;

import org.ors.subsystem.moderation.job_moderation.enums.EntityType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// audit_logs ở schema này chỉ có (user_id, action_type, description, created_at) - không có
// cột entity_type/entity_id như migration cá nhân trước đây. Thay vì đề xuất thêm cột vào
// bảng dùng chung, entityType/entityId được nhồi vào đầu description theo định dạng cố định
// "[entityType=X;entityId=Y] <text>", tự giải mã lại khi đọc (UC-46/UC-50 cần lọc theo entity).
//
// decode() trả entityType=null khi gặp dòng audit cũ không theo định dạng này (vd dòng
// "target=...; reason=..." mà AuditLogService của module Admin đang ghi) - không ném lỗi.
public final class AuditDescriptionCodec {

    private static final Pattern PREFIX_PATTERN =
            Pattern.compile("^\\[entityType=([^;]*);entityId=([^]]*)]\\s?(.*)$", Pattern.DOTALL);

    private AuditDescriptionCodec() {
    }

    public static String encode(EntityType entityType, Integer entityId, String description) {
        String prefix = "[entityType=" + (entityType == null ? "" : entityType.name())
                + ";entityId=" + (entityId == null ? "" : entityId) + "]";
        return description == null || description.isBlank() ? prefix : prefix + " " + description;
    }

    public static Decoded decode(String raw) {
        if (raw == null) {
            return new Decoded(null, null, null);
        }
        Matcher matcher = PREFIX_PATTERN.matcher(raw);
        if (!matcher.matches()) {
            return new Decoded(null, null, raw);
        }
        String entityType = matcher.group(1).isBlank() ? null : matcher.group(1);
        Integer entityId = matcher.group(2).isBlank() ? null : Integer.valueOf(matcher.group(2));
        return new Decoded(entityType, entityId, matcher.group(3));
    }

    public record Decoded(String entityType, Integer entityId, String message) {
    }
}
