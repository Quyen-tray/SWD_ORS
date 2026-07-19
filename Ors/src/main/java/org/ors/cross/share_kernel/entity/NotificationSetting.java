package org.ors.cross.share_kernel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "notification_settings")
public class NotificationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate;

    @ColumnDefault("1")
    @Column(name = "email_alerts")
    private Boolean emailAlerts;

    @ColumnDefault("0")
    @Column(name = "sms_alerts")
    private Boolean smsAlerts;

}