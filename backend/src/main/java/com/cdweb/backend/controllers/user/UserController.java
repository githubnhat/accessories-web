package com.cdweb.backend.controllers.user;

import com.cdweb.backend.common.Constant;
import com.cdweb.backend.common.JwtService;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.responses.*;
import com.cdweb.backend.services.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @DeleteMapping ("/address/{id}")
    ResponseEntity<?> deleteAddress(HttpServletRequest httpRequest, @PathVariable("id") Long id) {
        Users user = getUserFromRequest(httpRequest);

        boolean response = userService.deleteAddress(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                !response ?
                        new ResponseObject("Failed", "Delete address fail", false) :
                        new ResponseObject("Success", "Delete address successfully", true));
    }

    @PostMapping("/order")
    ResponseEntity<?> insertOrder(HttpServletRequest httpRequest, @RequestBody OrderRequest orderRequest) {
        Users user = getUserFromRequest(httpRequest);

        OrderResponse response = userService.save(orderRequest, user);
        return ResponseEntity.status(HttpStatus.OK).body(
               response == null  ?
                        new ResponseObject("Failed", "Order fail", null) :
                        new ResponseObject("Success", "Order successfully", response));
    }

    @GetMapping("/orders/page/{page}/limit/{limit}")
    ResponseEntity<?> getOrderByUser(HttpServletRequest httpRequest, @PathVariable("page") int page, @PathVariable("limit") int limit) {
        Users user = getUserFromRequest(httpRequest);

        Pageable pageable = PageRequest.of(page - 1, limit);
        int totalItem = userService.totalOrderByUser(user);

        PageResponse<OrderResponse> response = PageResponse.<OrderResponse>builder()
                .page(page)
                .totalPages((int) Math.ceil((double) (totalItem) / limit))
                .totalItems(totalItem)
                .data(userService.getOrders(pageable, user))
                .build();

        return  ResponseEntity.status(HttpStatus.OK).body(
                (response.getData().size() > 0)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Have no order", null));
    }

        private Users getUserFromRequest(HttpServletRequest httpRequest) {
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(Constant.BEARER.length());
        Users user = jwtService.getUserFromToken(token);
        return user;
    }
}