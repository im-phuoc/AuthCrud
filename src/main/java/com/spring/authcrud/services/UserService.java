package com.spring.authcrud.services;

import com.spring.authcrud.payload.request.UpdateRoleRequest;
import com.spring.authcrud.payload.response.PagedResponse;
import com.spring.authcrud.payload.response.UserResponse;

public interface UserService {
    PagedResponse<UserResponse> getAllUsers(int size, int page);
    UserResponse getInfo();
    void updateRole(String username, UpdateRoleRequest updateRoleRequest);
    void deleteUser(String username);
}
