package org.ors.subsystem.administration.user_management.state;

// State pattern cho vòng đời tài khoản (BR-13, NFR-FE07-1).
// Mỗi trạng thái là một class, tự trả lời cho riêng nó: activate / deactivate / ban thì
// đi đâu. Nước đi không hợp lệ KHÔNG phải là một nhánh if bị thiếu, mà là một method ném
// lỗi. Thêm trạng thái mới = thêm class, không phải sửa một chuỗi if mà 5 trạng thái kia
// cũng đang phụ thuộc vào.
//
// Phải đồng bộ với State machine diagram của User Account (báo cáo mục III.3).
public interface AccountState {

    AccountState activate();

    AccountState deactivate();

    AccountState ban();

    String status();
}
