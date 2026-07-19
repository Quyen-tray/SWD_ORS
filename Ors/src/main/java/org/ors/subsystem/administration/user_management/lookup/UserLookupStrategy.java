package org.ors.subsystem.administration.user_management.lookup;

import org.ors.cross.share_kernel.entity.User;

import java.util.List;

// Strategy pattern cho việc tra cứu người dùng (UC-53).
//
// UC-53 có 3 cách tra cứu khác nhau, đúng bằng 3 nhánh của khối alt trong Sequence
// Diagram UC-53 (báo cáo mục III.2): tìm theo từ khoá, lọc theo vai trò và trạng thái,
// hoặc không có tiêu chí nào thì lấy tất cả.
//
// Viết bằng if/else thì cả 3 cách tra cứu dính vào một method, và thêm cách thứ tư
// (vd lọc theo khoảng ngày tạo) là phải mở lại đúng cái method mà 3 cách kia đang
// phụ thuộc. Tách ra thì mỗi cách là một class: thêm cách mới = thêm class mới, không
// sửa class cũ.
//
// Mỗi chiến lược tự trả lời 2 câu: "tiêu chí này có phải việc của tôi không" (supports)
// và "nếu phải thì tra cứu thế nào" (lookup).
public interface UserLookupStrategy {

    boolean supports(UserCriteria criteria);

    List<User> lookup(UserCriteria criteria);
}
