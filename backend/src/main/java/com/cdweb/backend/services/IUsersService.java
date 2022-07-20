package com.cdweb.backend.services;

import com.cdweb.backend.entities.Address;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.UserRequest;
import com.cdweb.backend.payloads.responses.AddressResponse;
import com.cdweb.backend.payloads.responses.UserResponse;

import java.util.List;

public interface IUsersService {
    List<UserResponse> getAllUsers();

    AddressResponse findMainAddress(Users user);

    List<AddressResponse> getAllAddresses(Users user);

    AddressResponse save(AddressRequest address, Users user);

    Boolean deleteAddress(Long id, Users user);
}
