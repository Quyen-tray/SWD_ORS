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

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "interviews")
public class Interview {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "application_id", nullable = false)
    private org.ors.cross.share_kernel.entity.JobApplication application;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private org.ors.cross.share_kernel.entity.User creator;

    @NotNull
    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @Size(max = 255)
    @Nationalized
    @Column(name = "location")
    private String location;

    @Size(max = 500)
    @Nationalized
    @Column(name = "meeting_link", length = 500)
    private String meetingLink;

    @Size(max = 30)
    @NotNull
    @ColumnDefault("'SCHEDULED'")
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "round", nullable = false)
    private Integer round;

    @Size(max = 30)
    @Column(name = "outcome", length = 30)
    private String outcome;

    @Column(name = "rating", precision = 3, scale = 1)
    private BigDecimal rating;

    @Nationalized
    @Lob
    @Column(name = "comments")
    private String comments;

}