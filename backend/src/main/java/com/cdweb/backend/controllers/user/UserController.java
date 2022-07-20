package com.cdweb.backend.controllers.user;

import com.cdweb.backend.common.Constant;
import com.cdweb.backend.common.JwtService;
import com.cdweb.backend.entities.Address;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.requests.UserRequest;
import com.cdweb.backend.payloads.responses.AddressResponse;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController(value = "controllerForUser")
@RequestMapping("/api/v1/user/info")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final IUsersService userService;

    private final JwtService jwtService;

    @GetMapping("/address")
    ResponseEntity<?> getMainAddress(HttpServletRequest httpRequest) {
        Users user = getUserFromRequest(httpRequest);

        AddressResponse response = userService.findMainAddress(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                response == null ?
                        new ResponseObject("Failed", "User have no address", "") :
                        new ResponseObject("Success", "Get the main user address successfully", response));
    }

    @GetMapping ("/addresses")
    ResponseEntity<?> getAllAddress(HttpServletRequest httpRequest) {
        Users user = getUserFromRequest(httpRequest);

        List<AddressResponse> response = userService.getAllAddresses(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                response == null ?
                        new ResponseObject("Failed", "User have no address", "") :
                        new ResponseObject("Success", "Get all user addresses successfully", response));
    }

    @PostMapping ("/address")
    ResponseEntity<?> insertAddress(HttpServletRequest httpRequest, @RequestBody AddressRequest request) {
        Users user = getUserFromRequest(httpRequest);

        AddressResponse response = userService.save(request, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                response == null ?
                        new ResponseObject("Failed", "Insert address fail", "") :
                        new ResponseObject("Success", "Insert user address successfully", response));
    }

    private Users getUserFromRequest(HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(Constant.BEARER.length());
        Users user = jwtService.getUserFromToken(token);
        return user;
    }
}