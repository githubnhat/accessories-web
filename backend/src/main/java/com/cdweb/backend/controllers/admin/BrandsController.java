package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.payloads.requests.BrandRequest;
import com.cdweb.backend.payloads.requests.PagesRequest;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.payloads.responses.CategoryResponse;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController(value="brandsControllerOfAdmin")
@RequestMapping("/api/v1/admin/brand")
@RequiredArgsConstructor
@Slf4j
public class BrandsController {

    public final IBrandService brandService;

    @PostMapping("/list")
    ResponseEntity<?> getAll(@RequestBody PagesRequest request) {
        PageResponse<BrandResponse> response = new PageResponse<>();
        response.setPage(request.getPage());
        Pageable pageable;
        if (request.getSort() != null) {
            List<Sort.Order> orders = new ArrayList<>();
            request.getSort().forEach(e -> {
                orders.add(new Sort.Order(
                        e.getSortDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                        e.getSortBy()));
            });
            pageable = PageRequest.of(request.getPage() - 1, request.getLimit(), Sort.by(orders));
        } else {
            pageable = PageRequest.of(request.getPage() - 1, request.getLimit(),
                    Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        }
        int totalItem = brandService.totalItem();
        response.setTotalItems(totalItem);
        response.setTotalPages((int) Math.ceil((double) (totalItem) / request.getLimit()));
        response.setData(brandService.findAll(pageable));
        return response.getData().size() > 0
                ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Success", null, response))
                :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("Failed", "Have no brand", null));
    }

    @GetMapping("")
    ResponseEntity<?> getAllIsActiveBrands(){
        List<BrandResponse> response = brandService.findByIsActiveTrue();
        return ResponseEntity.status(HttpStatus.OK).body(
                (response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Have no brand", null));
    }

    @PostMapping("")
    ResponseEntity<?> insertBrand(@RequestBody BrandRequest request) {
        BrandResponse response = brandService.save(request);
        return
                ResponseEntity.status(HttpStatus.OK).body((response != null)
                        ? new ResponseObject("Success", "Insert Brand Successfully", response)
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

    @GetMapping("/exists/{name}")
    ResponseEntity<?> existsBrandByName(@PathVariable("name") String name) {
        log.info("pn {}", name);
        Boolean exists = brandService.existsByNameAndIsActiveTrue(name);
        log.info("kq {}", exists);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "", exists));
    }

    @GetMapping("/existsCode/{code}")
    ResponseEntity<?> existsBrandByCode(@PathVariable("code") String code) {
        log.info("pn {}", code);
        Boolean exists = brandService.existsByCodeAndIsActiveTrue(code);
        log.info("kq {}", exists);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "", exists));
    }

    @DeleteMapping("")
    ResponseEntity<?> deleteBrand(@RequestBody Long[] ids) {
        return ResponseEntity.status(HttpStatus.OK).body(brandService.delete(ids) ?
                new ResponseObject("Success", "Delete brand successfully", true) :
                new ResponseObject("Failed", "Can not find brand", false));
    }
}
