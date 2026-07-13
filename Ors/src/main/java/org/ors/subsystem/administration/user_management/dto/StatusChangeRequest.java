package org.ors.subsystem.administration.user_management.dto;

// Body của thao tác Deactivate và Ban. BR-13 bắt buộc phải ghi lý do.
public record StatusChangeRequest(String reason) {
}
