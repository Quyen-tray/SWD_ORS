package org.ors.subsystem.administration.user_management.state;

import org.ors.cross.share_kernel.entity.UserStatus;
import org.ors.cross.share_kernel.exception.BadRequestException;

// 5 trạng thái của UserStatus và nhà máy dựng state từ trạng thái hiện tại của tài khoản.
// ACTIVE / INACTIVE / BANNED là 3 trạng thái Admin thao tác được (UC-54, UC-55, UC-56).
// EMAIL_PENDING thuộc luồng đăng ký, LOCKED thuộc module xác thực (khoá do đăng nhập sai
// nhiều lần) - BR-13 không cho Admin chuyển trạng thái từ hai chỗ này, nên hai class đó
// từ chối mọi thao tác quản trị.
//
// Vì tham số là enum chứ không phải String, nhánh switch dưới đây VÉT CẠN mọi trạng thái:
// thêm một trạng thái mới vào UserStatus mà quên xử lý ở đây thì trình biên dịch báo lỗi
// ngay, không cần nhánh default để bắt "trạng thái lạ" nữa.
public final class AccountStates {

    private AccountStates() {
    }

    public static AccountState of(UserStatus status) {
        if (status == null) {
            throw new BadRequestException("Tài khoản không có trạng thái hợp lệ");
        }
        return switch (status) {
            case ACTIVE -> new ActiveState();
            case INACTIVE -> new InactiveState();
            case BANNED -> new BannedState();
            case EMAIL_PENDING -> new EmailPendingState();
            case LOCKED -> new LockedState();
        };
    }

    public static final class ActiveState implements AccountState {
        public AccountState activate() {
            throw new BadRequestException("Tài khoản đang hoạt động rồi");
        }

        public AccountState deactivate() {
            return new InactiveState();
        }

        public AccountState ban() {
            return new BannedState();
        }

        public UserStatus status() {
            return UserStatus.ACTIVE;
        }
    }

    public static final class InactiveState implements AccountState {
        public AccountState activate() {
            return new ActiveState();
        }

        public AccountState deactivate() {
            throw new BadRequestException("Tài khoản đã bị tạm ngưng rồi");
        }

        public AccountState ban() {
            return new BannedState();
        }

        public UserStatus status() {
            return UserStatus.INACTIVE;
        }
    }

    public static final class BannedState implements AccountState {
        // Admin gỡ cấm bằng chính thao tác Activate (UC-54).
        public AccountState activate() {
            return new ActiveState();
        }

        public AccountState deactivate() {
            throw new BadRequestException("Không thể tạm ngưng một tài khoản đã bị cấm");
        }

        public AccountState ban() {
            throw new BadRequestException("Tài khoản đã bị cấm rồi");
        }

        public UserStatus status() {
            return UserStatus.BANNED;
        }
    }

    public static final class EmailPendingState implements AccountState {
        public AccountState activate() {
            throw new BadRequestException("Tài khoản chưa xác thực email");
        }

        public AccountState deactivate() {
            throw new BadRequestException("Tài khoản chưa được kích hoạt");
        }

        public AccountState ban() {
            throw new BadRequestException("Tài khoản chưa được kích hoạt");
        }

        public UserStatus status() {
            return UserStatus.EMAIL_PENDING;
        }
    }

    public static final class LockedState implements AccountState {
        public AccountState activate() {
            throw new BadRequestException("Tài khoản đang bị hệ thống khoá do đăng nhập sai nhiều lần");
        }

        public AccountState deactivate() {
            throw new BadRequestException("Tài khoản đang bị hệ thống khoá");
        }

        public AccountState ban() {
            throw new BadRequestException("Tài khoản đang bị hệ thống khoá");
        }

        public UserStatus status() {
            return UserStatus.LOCKED;
        }
    }
}
