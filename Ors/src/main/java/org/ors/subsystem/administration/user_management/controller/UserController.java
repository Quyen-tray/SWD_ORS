package org.ors.subsystem.administration.user_management.controller;

import org.ors.subsystem.administration.user_management.dto.StatusChangeRequest;
import org.ors.subsystem.administration.user_management.dto.UserResponse;
import org.ors.subsystem.administration.user_management.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// UC-53..56. Controller chỉ ánh xạ HTTP sang lời gọi service, không chứa business rule.
@RestController
@RequestMapping("/admin/user-management")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    // UC-53 - xem danh sách, kèm tìm kiếm và lọc.
    @GetMapping
    public List<UserResponse> getUsers(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String role,
                                       @RequestParam(required = false) String status) {
        return userService.getUsers(keyword, role, status);
    }

    // UC-54
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Integer id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    // UC-55
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer id,
                                               @RequestBody StatusChangeRequest request) {
        userService.deactivateUser(id, request.reason());
        return ResponseEntity.noContent().build();
    }

    // UC-56
    @PutMapping("/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Integer id,
                                        @RequestBody StatusChangeRequest request) {
        userService.banUser(id, request.reason());
        return ResponseEntity.noContent().build();
    }
}
