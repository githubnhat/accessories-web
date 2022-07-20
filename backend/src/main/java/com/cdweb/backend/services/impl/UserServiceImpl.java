package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.AddressConverter;
import com.cdweb.backend.entities.Address;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.UserRequest;
import com.cdweb.backend.payloads.responses.AddressResponse;
import com.cdweb.backend.payloads.responses.UserResponse;
import com.cdweb.backend.repositories.AddressRepository;
import com.cdweb.backend.repositories.UserRepository;
import com.cdweb.backend.services.IUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUsersService {
    private final UserRepository usersRepository;

    private final AddressRepository addressRepository;

    private final AddressConverter addressConverter;

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserResponse> response = new ArrayList<>();
        List<Users> entities = usersRepository.findByIsActiveTrue();
        for (Users user: entities) {
            response.add(UserResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .gmail(user.getGmail())
                    .roles(user.getRoles().getRoleName())
                    .createdBy(user.getCreatedBy())
                    .createdDate(user.getCreatedDate())
                    .modifiedBy(user.getModifiedBy())
                    .modifiedDate(user.getModifiedDate())
                    .build());
        }
        return response;
    }

    @Override
    public AddressResponse findMainAddress(Users user) {
        Address address = addressRepository.findByUserAndIsMainAddressTrue(user);
        AddressResponse response = addressConverter.toResponse(address);
        return response;
    }

    @Override
    public List<AddressResponse> getAllAddresses(Users user) {
        List<Address> addresses = addressRepository.findByUserOrderByCreatedDateDesc(user);
        List<AddressResponse> responses = new ArrayList<>();
        for (Address address: addresses) {
            AddressResponse response = addressConverter.toResponse(address);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public AddressResponse save(AddressRequest request, Users user) {
        if(request.getIsMainAddress() == true) {
            Address address = addressRepository.findByUserAndIsMainAddressTrue(user);
            if(address!=null){
                address.setIsMainAddress(false);
                addressRepository.save(address);
            }
        }
        Address address = addressRepository.save(Address.builder()
                        .address(request.getAddress())
                        .isMainAddress(request.getIsMainAddress())
                        .phone(request.getPhone())
                        .user(user)
                .build());
        AddressResponse response = addressConverter.toResponse(address);
        return response;
    }
}
