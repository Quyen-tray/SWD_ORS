package org.ors.cross.share_kernel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Cột users.role và users.status là NVARCHAR có ràng buộc CHECK, tức chỉ nhận đúng một
    // tập giá trị cố định. Dùng String thì gõ sai ("ACTIVEE") phải đợi tới lúc chạy mới bị
    // SQL Server từ chối. Dùng enum + @Enumerated(EnumType.STRING) thì trình biên dịch bắt
    // ngay, mà cột trong DB vẫn là NVARCHAR như cũ - KHÔNG phải đổi schema.
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private RoleName role;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'EMAIL_PENDING'")
    @Column(name = "status", length = 50)
    private UserStatus status;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    private Instant updatedAt;

}