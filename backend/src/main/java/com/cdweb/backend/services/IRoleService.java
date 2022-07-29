package com.cdweb.backend.services;

import com.cdweb.backend.payloads.requests.RoleRequest;
import com.cdweb.backend.payloads.responses.RoleResponse;

import java.util.List;

public interface IRoleService {
//    List<RoleResponse> getAllRoles();
    RoleResponse create(RoleRequest request);
    RoleResponse update(RoleRequest request, Long id);

   List<RoleResponse> getAllRoles();
//    void delete(Long id);

}
