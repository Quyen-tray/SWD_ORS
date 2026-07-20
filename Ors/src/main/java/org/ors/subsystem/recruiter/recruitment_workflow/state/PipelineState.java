package org.ors.subsystem.recruiter.recruitment_workflow.state;

// State pattern cho vòng đời JobApplication.status (UC-04, tái dùng ở UC-07 - xem
// 00_KE_HOACH_TONG_QUAN.md mục 2.2). Giống hệt cách AccountState đã làm ở
// administration.user_management.state: mỗi trạng thái là một class, tự trả lời cho
// riêng nó "đi tiếp thì tới đâu" / "từ chối thì có được không". Nước đi sai ném lỗi
// ngay tại state, không phải một nhánh if bị thiếu ở service.
//
// Chỉ có 2 nước đi mà Recruiter được chủ động thực hiện qua UC-04/UC-07:
//   - advance(): đẩy đơn ứng tuyển sang giai đoạn kế tiếp trong pipeline (tuần tự,
//     không cho nhảy cóc - vd đang SUBMITTED thì không thể nhảy thẳng lên OFFERED).
//   - reject(): từ chối, có thể xảy ra ở bất kỳ giai đoạn nào chưa kết thúc (đúng như
//     mô tả trong frontend_demo/uc04-pipeline-status.html: "Trạng thái Rejected có thể
//     đến từ bất kỳ giai đoạn nào"). Lý do từ chối do service validate và ghi vào
//     history, không phải việc của state.
// WITHDRAWN không nằm trong 2 nước đi này: đó là do Candidate tự rút hồ sơ (module
// candidate, ngoài scope UC-04), Recruiter không có quyền đẩy đơn vào/ra khỏi trạng
// thái này - xem WithdrawnState.
public interface PipelineState {

    PipelineState advance();

    PipelineState reject();

    String status();
}
