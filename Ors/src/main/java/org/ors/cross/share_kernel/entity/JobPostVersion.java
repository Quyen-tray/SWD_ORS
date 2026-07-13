package org.ors.cross.share_kernel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "job_post_versions")
public class JobPostVersion {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "job_post_id", nullable = false)
    private org.ors.cross.share_kernel.entity.JobPost jobPost;

    @Nationalized
    @Lob
    @Column(name = "version_data")
    private String versionData;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

}