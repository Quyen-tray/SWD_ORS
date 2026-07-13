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
@Table(name = "companies")
public class Company {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'DRAFT'")
    @Column(name = "verification_status", length = 50)
    private String verificationStatus;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Nationalized
    @Column(name = "website")
    private String website;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 50)
    @Nationalized
    @Column(name = "tax_code", length = 50)
    private String taxCode;

    @Size(max = 500)
    @Nationalized
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Size(max = 500)
    @Nationalized
    @Column(name = "address", length = 500)
    private String address;

}