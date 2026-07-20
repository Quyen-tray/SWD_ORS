package org.ors.subsystem.administration.user_management.service;

import org.ors.subsystem.administration.user_management.dto.UserDetailResponse;
import org.ors.subsystem.administration.user_management.dto.UserResponse;

import java.util.List;

// Controller phụ thuộc vào interface này chứ không phụ thuộc vào UserService,
// nên có thể thay hoặc mock phần hiện thực mà không đụng tới controller.
public interface IUserService {

    // UC-53: xem, tìm theo từ khoá, lọc theo vai trò và trạng thái.
    List<UserResponse> getUsers(String keyword, String role, String status);

    // UC-54
    void activateUser(Integer userId);

    // UC-55
    void deactivateUser(Integer userId, String reason);

    // UC-56
    void banUser(Integer userId, String reason);

    // UC-62: hồ sơ chi tiết + lịch sử thao tác của một người dùng.
    UserDetailResponse getUserDetail(Integer userId);
}
