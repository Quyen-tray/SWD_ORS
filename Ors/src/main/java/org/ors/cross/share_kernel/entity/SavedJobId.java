package org.ors.cross.share_kernel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SavedJobId implements java.io.Serializable {
    private static final long serialVersionUID = -4332005373983687470L;
    @NotNull
    @Column(name = "candidate_id", nullable = false)
    private Integer candidateId;

    @NotNull
    @Column(name = "job_post_id", nullable = false)
    private Integer jobPostId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SavedJobId entity = (SavedJobId) o;
        return Objects.equals(this.jobPostId, entity.jobPostId) &&
                Objects.equals(this.candidateId, entity.candidateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobPostId, candidateId);
    }

}