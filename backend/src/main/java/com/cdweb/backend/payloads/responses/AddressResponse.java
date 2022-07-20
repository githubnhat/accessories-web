package com.cdweb.backend.payloads.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressResponse {
    private Long id;
    private String address;
    private Boolean isMainAddress;
    private String phone;
}
