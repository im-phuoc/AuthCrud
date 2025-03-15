package com.spring.authcrud.controllers;

import com.spring.authcrud.payload.request.UpdateRoleRequest;
import com.spring.authcrud.payload.response.ApiResponse;
import com.spring.authcrud.payload.response.PagedResponse;
import com.spring.authcrud.payload.response.UserResponse;
import com.spring.authcrud.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<
            ApiResponse<PagedResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(new ApiResponse<>(true,"success",userService.getAllUsers(page,size)));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile() {
        return ResponseEntity.ok().body(new ApiResponse<>(true,"Success",userService.getInfo()));

    }

    @PutMapping("/{username}")
    public ResponseEntity<ApiResponse<?>> updateRole(@PathVariable String username, @RequestBody UpdateRoleRequest updateRoleRequest) {
        if (updateRoleRequest!=null) {
            userService.updateRole(username,updateRoleRequest);
        }

        return ResponseEntity.ok().body(new ApiResponse<>(true,"Success",userService.getInfo()));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().body(new ApiResponse<>(true,"User deleted successfully!",null));
    }
}
