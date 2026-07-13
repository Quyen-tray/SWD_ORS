package org.ors.cross.share_kernel.entity;

// 4 vai trò của cột users.role, đúng bằng 4 giá trị mà ràng buộc CHECK trong ors.sql cho phép.
// Chỉ ADMIN mới truy cập được các chức năng của FE-07.
public enum RoleName {
    CANDIDATE,
    RECRUITER,
    MODERATOR,
    ADMIN
}
