package org.ors.subsystem.administration.user_management.state;

import org.ors.cross.share_kernel.exception.BadRequestException;

// 5 trạng thái của users.status (ors.sql) và nhà máy dựng state từ chuỗi trong DB.
// ACTIVE / INACTIVE / BANNED là 3 trạng thái Admin thao tác được (UC-54, UC-55, UC-56).
// EMAIL_PENDING thuộc luồng đăng ký, LOCKED thuộc module xác thực (khoá do đăng nhập sai
// nhiều lần) - BR-13 không cho Admin chuyển trạng thái từ hai chỗ này, nên hai class đó
// từ chối mọi thao tác quản trị.
public final class AccountStates {

    private AccountStates() {
    }

    public static AccountState of(String status) {
        if (status == null) {
            throw new BadRequestException("Tài khoản không có trạng thái hợp lệ");
        }
        return switch (status) {
            case "ACTIVE" -> new ActiveState();
            case "INACTIVE" -> new InactiveState();
            case "BANNED" -> new BannedState();
            case "EMAIL_PENDING" -> new EmailPendingState();
            case "LOCKED" -> new LockedState();
            default -> throw new BadRequestException("Trạng thái tài khoản không hợp lệ: " + status);
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

        public String status() {
            return "ACTIVE";
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

        public String status() {
            return "INACTIVE";
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

        public String status() {
            return "BANNED";
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

        public String status() {
            return "EMAIL_PENDING";
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

        public String status() {
            return "LOCKED";
        }
    }
}
