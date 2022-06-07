package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.payloads.requests.BrandRequest;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/brand")
@RequiredArgsConstructor
@Slf4j
public class BrandsController {

    public final IBrandService brandService;

    @GetMapping("/page/{page}/limit/{limit}")
    ResponseEntity<?> getAll(@PathVariable("page") int page, @PathVariable("limit") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        PageResponse<BrandResponse> response = PageResponse.<BrandResponse>builder()
                .page(page)
                .totalPage((int) Math.ceil((double) (brandService.totalItem()) / limit))
                .data(brandService.findAll(pageable))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(
                (response.getData() != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Have no brand", null));
    }

    @PostMapping("")
    ResponseEntity<?> insertBrand(@RequestBody BrandRequest request) {
        BrandResponse response = brandService.save(request);
        return
                ResponseEntity.status(HttpStatus.OK).body((response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Failed", "Brand name already taken", null));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateBrand(@PathVariable("id") Long id,@RequestBody BrandRequest request) {
        request.setId(id);
        BrandResponse response = brandService.save(request);
        return
                ResponseEntity.status(HttpStatus.OK).body((response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Failed", "Brand name already taken", null));
    }
}
