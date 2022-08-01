package com.cdweb.backend.controllers.user;

import com.cdweb.backend.payloads.requests.ProductCombinationRequest;
import com.cdweb.backend.payloads.responses.*;
import com.cdweb.backend.services.IProductCombinationService;
import com.cdweb.backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "productControllerOfUser")
@RequestMapping("/api/v1/user/product")
@RequiredArgsConstructor
@Slf4j

public class ProductController {

    private final IProductService productService;

    private final IProductCombinationService productCombinationService;

    @GetMapping("/no-token/page/{page}/limit/{limit}")
    ResponseEntity<?> getAllProduct(@PathVariable("page") int page, @PathVariable("limit") int limit) {
        PageResponse<ProductResponse> response = new PageResponse<>();
        response.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        response.setTotalPages((int) Math.ceil((double) (productService.totalItem()) / limit));
        response.setData(productService.findAllForUser(pageable));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response));
    }

    @GetMapping("/no-token/page/{page}/limit/{limit}/category/{code}")
    ResponseEntity<?> getProductByCategoryCode(@PathVariable("page") int page, @PathVariable("limit") int limit,
            @PathVariable("code") String code) {
        PageResponse<ProductResponse> response = new PageResponse<>();
        response.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
        int totalPages = productService.totalProductByCategoryCode(code);
        response.setTotalPages((int) Math.ceil((double) (totalPages) / limit));
        response.setData(productService.findAllByCategoryCodeForUser(code, pageable));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response));
    }

    @GetMapping("/no-token/page/{page}/limit/{limit}/brand/{code}")
    ResponseEntity<?> getProductByBrandCode(@PathVariable("page") int page,
            @PathVariable("limit") int limit, @PathVariable("code") String code) {
        PageResponse<ProductResponse> response = new PageResponse<>();
        response.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
        int totalPages = productService.totalProductByBrandCode(code);
        response.setTotalPages((int) Math.ceil((double) (totalPages) / limit));
        response.setData(productService.findAllByBrandCodeForUser(code, pageable));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response));
    }

    @GetMapping("/no-token/{id}")
    ResponseEntity<?> getProductDetails(@PathVariable("id") Long productId) {
        ProductResponse response = productService.findByProductId(productId);
        return response != null
                ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response))
                : ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("Fail", "Can not find product!", response));
    }

    @PostMapping("/no-token/productCombination")
    ResponseEntity<?> getProductCombination(@RequestBody ProductCombinationRequest request) {
        ProductCombinationResponse response = productCombinationService.findByProductIdAndUniqueStringId(request);
        return response != null
                ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response))
                : ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Fail", null, ""));
    }

    @GetMapping("/no-token/search/page/{page}/limit/{limit}/key/{keyword}")
    ResponseEntity<?> findByKeyword(@PathVariable("page") int page,
            @PathVariable("limit") int limit,
            @PathVariable("keyword") String keyword) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        int totalItem = productService.totalItemSearch(keyword, keyword);
        PageResponse<ProductResponse> response = PageResponse.<ProductResponse>builder()
                .page(page)
                .totalPages((int) Math.ceil((double) (totalItem) / limit))
                .totalItems(totalItem)
                .data(productService
                        .searchProducts(keyword, pageable))
                .build();
        return response != null
                ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response))
                : ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Fail", null, ""));
    }
}
