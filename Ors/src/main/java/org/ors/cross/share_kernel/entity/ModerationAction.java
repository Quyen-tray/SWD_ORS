package org.ors.cross.share_kernel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "moderation_actions")
public class ModerationAction {
    // moderation_actions.id là INT IDENTITY trong SQL Server (xem chú thích tương tự ở
    // JobCategory/AuditLog). Không có @GeneratedValue thì Hibernate cố INSERT id = null khi
    // tạo ModerationAction mới -> UC-47/48 (resolve/close) sẽ lỗi ngay lần ghi đầu tiên.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private JobReport report;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "moderator_id", nullable = false)
    private org.ors.cross.share_kernel.entity.User moderator;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "action_taken", nullable = false)
    private String actionTaken;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

}