package org.ors.cross.share_kernel.entity;

// 5 trạng thái của cột users.status. Đúng bằng 5 giá trị mà ràng buộc CHECK trong ors.sql
// cho phép, nên gõ sai một chữ là trình biên dịch bắt ngay, không phải đợi SQL Server từ chối
// lúc chạy.
//
// ACTIVE / INACTIVE / BANNED là 3 trạng thái Admin chuyển qua lại (UC-54, UC-55, UC-56).
// EMAIL_PENDING thuộc luồng đăng ký, LOCKED thuộc module xác thực (khoá do đăng nhập sai
// nhiều lần) - BR-13 không cho Admin thao tác lên hai trạng thái này.
//
// Ánh xạ xuống DB bằng @Enumerated(EnumType.STRING) nên cột vẫn là NVARCHAR như cũ,
// KHÔNG phải đổi schema.
public enum UserStatus {
    EMAIL_PENDING,
    ACTIVE,
    INACTIVE,
    BANNED,
    LOCKED
}
