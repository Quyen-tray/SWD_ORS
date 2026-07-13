package org.ors.cross.share_kernel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "job_categories")
public class JobCategory {
    // job_categories.id là INT IDENTITY trong SQL Server. Không có @GeneratedValue thì
    // Hibernate coi id là do code tự gán và cố INSERT id = null -> tạo danh mục mới sẽ lỗi.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "category_name", nullable = false)
    private String categoryName;

}