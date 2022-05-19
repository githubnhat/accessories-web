package com.cdweb.backend.services;

import com.cdweb.backend.payloads.requests.AuthRequest;
import com.cdweb.backend.payloads.requests.RegistrationRequest;
import com.cdweb.backend.payloads.responses.AuthResponse;


public interface IAuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegistrationRequest request);
}
