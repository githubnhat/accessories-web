package com.cdweb.backend.controllers;

import com.cdweb.backend.common.JwtService;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.*;
import com.cdweb.backend.payloads.responses.AuthResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.payloads.responses.UserResponse;
import com.cdweb.backend.services.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final IAuthService authService;

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(request);
            Users user= jwtService.getUserFromToken(authResponse.getAccessToken());
            addRefreshTokenToCookie(response, user);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", null, authResponse));
        } catch (IllegalArgumentException ex) {
            log.error("API /login: {}", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", ex.getMessage(),null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request, HttpServletResponse response) {
        try {
            UserResponse userResponse = authService.register(request);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, userResponse));
        } catch (IllegalArgumentException ex) {
            log.error("API /register: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Fail", ex.getMessage(),null));
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Fail", ex.getMessage(),null));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Fail", ex.getMessage(),null));
        }
    }


    @PostMapping("/confirm")
    public ResponseEntity<?> confirmOTP(@RequestBody ConfirmRequest request, HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.confirmOTP(request);
            Users user= jwtService.getUserFromToken(authResponse.getAccessToken());
            addRefreshTokenToCookie(response, user);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, authResponse));
        } catch (IllegalArgumentException ex) {
            log.error("API /register: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Fail", ex.getMessage(),null));
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh_token", required = false) Cookie tokenCookie) {
        if (tokenCookie == null || jwtService.isNoneValidRefreshToken(tokenCookie.getValue())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseObject("Fail", "Refresh token đã hết hạn!", null));
        } else {
            Users user = jwtService.getUserFromToken(tokenCookie.getValue());
            String accessToken = jwtService.generateAccessToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Success", null, AuthResponse.builder().accessToken(accessToken).build()));
        }
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, Users user) {
        String refresh_token = jwtService.generateRefreshToken(user);
        Cookie cookie = new Cookie("refresh_token", refresh_token);
        cookie.setHttpOnly(true);
        cookie.setPath("/1");
        cookie.setMaxAge(jwtService.getRefreshTokenLifeTimeHours()*60);
        response.addCookie(cookie);
        log.info("Refresh token {}", cookie.getClass());
    }

    @GetMapping("/check-username")
    ResponseEntity<?> existsProductByName(@RequestBody UserRequest request) {
        Boolean exists = authService.existsByUserName(request.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "", exists));
    }

    @GetMapping("/check-gmail")
    ResponseEntity<?> existsProductByGmail(@RequestBody UserRequest request) {
        Boolean exists = authService.existsByGmail(request.getGmail());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "", exists));
    }
}
