package com.cdweb.backend.services.impl;

import com.cdweb.backend.entities.Roles;
import com.cdweb.backend.payloads.requests.RoleRequest;
import com.cdweb.backend.payloads.responses.RoleResponse;
import com.cdweb.backend.repositories.RoleRepository;
import com.cdweb.backend.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements IRoleService {
    private final RoleRepository rolesRepository;


    @Override
    public RoleResponse create(RoleRequest request) {
        Roles entity = rolesRepository.findByRoleCode(request.getRoleCode());
        if (entity != null) {
            throw new IllegalArgumentException("Role name already exists!");
        }
        entity = rolesRepository.save(Roles.builder().roleCode(request.getRoleCode()).build());
        entity.setModifiedBy(null);
        entity.setCreatedDate(null);
        return RoleResponse.builder()
                .roleName(entity.getRoleName())
                .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .createdDate(entity.getCreatedDate())
                .modifiedBy(entity.getModifiedBy())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }

    @Override
    public RoleResponse update(RoleRequest request, Long id) {
        Roles entity = rolesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        if (request.getRoleCode() != null && request.getRoleCode().length() > 0 && !Objects.equals(entity.getRoleName(), request.getRoleCode())) {
            entity.setRoleName(request.getRoleCode());
        }
        Roles updatedRole = rolesRepository.save(entity);
        return RoleResponse.builder()
                .roleName(updatedRole.getRoleName())
                .id(updatedRole.getId())
                .createdBy(updatedRole.getCreatedBy())
                .createdDate(updatedRole.getCreatedDate())
                .modifiedBy(updatedRole.getModifiedBy())
                .modifiedDate(updatedRole.getModifiedDate())
                .build();
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleResponse> responses = new ArrayList<>();
        List<Roles> roles = rolesRepository.findAll();
        if (roles.size() > 0) {
            roles.forEach(r -> {
                responses.add(RoleResponse.builder()
                        .id(r.getId())
                        .roleName(r.getRoleName())
                        .roleCode(r.getRoleCode())
                        .build());
            });
            return responses;
        }
        return null;
    }


//    @Override
//    public void delete(Long id) {
//       Roles entity = rolesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
//        if (entity.isActive()) {
//            entity.setActive(false);
//            rolesRepository.save(entity);
//        } else {
//            throw new IllegalArgumentException("Role is not exist");
//        }
//    }
}
