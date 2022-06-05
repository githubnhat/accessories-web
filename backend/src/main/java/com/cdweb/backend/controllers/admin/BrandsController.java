package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.payloads.requests.BrandRequest;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/brand")
@RequiredArgsConstructor
@Slf4j
public class BrandsController {

    public final IBrandService brandService;

    @GetMapping("")
    ResponseEntity<?> getAll() {
        List<BrandResponse> response = brandService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response));
    }
}
