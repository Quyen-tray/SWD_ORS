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
@Table(name = "communications")
public class Communication {
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
    @JoinColumn(name = "sender_id", nullable = false)
    private org.ors.cross.share_kernel.entity.User sender;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @ColumnDefault("getdate()")
    @Column(name = "sent_at")
    private Instant sentAt;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'MESSAGE'")
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Size(max = 255)
    @Nationalized
    @Column(name = "subject")
    private String subject;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;


}