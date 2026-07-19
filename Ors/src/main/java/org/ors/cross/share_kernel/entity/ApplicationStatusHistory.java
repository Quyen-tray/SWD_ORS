package org.ors.cross.share_kernel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "application_status_histories")
public class ApplicationStatusHistory {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "application_id", nullable = false)
    private org.ors.cross.share_kernel.entity.JobApplication application;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @ColumnDefault("getdate()")
    @Column(name = "changed_at")
    private Instant changedAt;

    @Size(max = 500)
    @Nationalized
    @Column(name = "reason", length = 500)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User changedBy;

}