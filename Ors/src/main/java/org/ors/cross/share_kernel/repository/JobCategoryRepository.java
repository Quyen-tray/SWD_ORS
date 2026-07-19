package org.ors.cross.share_kernel.repository;

import org.ors.cross.share_kernel.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Integer> {

    // BR-14: tên danh mục phải là duy nhất.
    boolean existsByCategoryNameIgnoreCase(String categoryName);

    // Dùng khi sửa tên: tên mới không được trùng với danh mục KHÁC (trùng với chính nó thì hợp lệ).
    boolean existsByCategoryNameIgnoreCaseAndIdNot(String categoryName, Integer id);
}
