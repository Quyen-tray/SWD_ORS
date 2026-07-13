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
@Table(name = "cvs")
public class Cv {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "cv_name", nullable = false)
    private String cvName;

    @Size(max = 500)
    @Nationalized
    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

}