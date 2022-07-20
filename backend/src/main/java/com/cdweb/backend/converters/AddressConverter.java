package com.cdweb.backend.converters;

import com.cdweb.backend.entities.Address;
import com.cdweb.backend.entities.Attributes;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.responses.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {

    public Address toEntity(AddressRequest request){
        return Address.builder()
                .address(request.getAddress())
                .isMainAddress(request.getIsMainAddress())
                .build();
    }

    public AddressResponse toResponse(Address address){
        return AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .isMainAddress(address.getIsMainAddress())
                .phone(address.getPhone())
                .build();
    }
}
