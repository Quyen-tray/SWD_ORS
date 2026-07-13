package org.ors.cross.share_kernel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "company_verification_documents")
public class CompanyVerificationDocument {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "request_id", nullable = false)
    private org.ors.cross.share_kernel.entity.CompanyVerificationRequest request;

    @Size(max = 500)
    @NotNull
    @Nationalized
    @Column(name = "document_url", nullable = false, length = 500)
    private String documentUrl;

    @Size(max = 100)
    @Nationalized
    @Column(name = "document_type", length = 100)
    private String documentType;

}