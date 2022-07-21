package com.cdweb.backend.controllers.user;

import com.cdweb.backend.common.Constant;
import com.cdweb.backend.common.JwtService;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.CartRequest;
import com.cdweb.backend.payloads.responses.CartResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
public class CartController {
    private final JwtService jwtService;

    private final ICartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request, HttpServletRequest httpRequest){
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(Constant.BEARER.length());
        Users user =  jwtService.getUserFromToken(token);
        CartResponse response = cartService.addToCart(request, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                (response != null)
                        ? new ResponseObject("Success", "Add to cart successfully", response)
                        : new ResponseObject("Success", null, null));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody CartRequest request){
        CartResponse response = cartService.updateCart(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                (response != null)
                        ? new ResponseObject("Success", "Update cart successfully", response)
                        : new ResponseObject("Success", null, null));
    }
    @GetMapping("/list-cart")
    public ResponseEntity<?> getListCart(HttpServletRequest httpRequest){
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(Constant.BEARER.length());
        Users user =  jwtService.getUserFromToken(token);
        List<CartResponse> responses = cartService.findByUserOrderByCreatedDateDesc(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                (responses.size()> 0)
                        ? new ResponseObject("Success", "Get list cart successfully", responses)
                        : new ResponseObject("Success", "There are no items available", null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCart(@RequestBody Long[] ids, HttpServletRequest httpRequest){
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(Constant.BEARER.length());
        Users user =  jwtService.getUserFromToken(token);
        boolean response = cartService.delete(ids, user);
        return ResponseEntity.status(HttpStatus.OK).body(
                (response)
                        ? new ResponseObject("Success", "Get list cart successfully", response)
                        : new ResponseObject("Success", "There are no items available", null));
    }
}
