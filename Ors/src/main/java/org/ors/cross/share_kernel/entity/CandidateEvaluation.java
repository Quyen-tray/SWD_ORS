package org.ors.cross.share_kernel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "candidate_evaluations")
public class CandidateEvaluation {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "application_id", nullable = false)
    private org.ors.cross.share_kernel.entity.JobApplication application;

    @Nationalized
    @Lob
    @Column(name = "evaluation_notes")
    private String evaluationNotes;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

}