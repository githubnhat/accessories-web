package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.converters.ProductConverter;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final IProductService productService;

    private final ProductConverter productConverter;

    // this request is: http://localhost:8081/api/v1/products?page=1&limit=3
    @GetMapping("")
    ResponseEntity<?> showProduct(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        PageResponse<ProductResponse> response = new PageResponse<>();
        response.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        response.setTotalPage((int) Math.ceil((double) (productService.totalItem()) / limit));
        response.setData(productService.findAll(pageable));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", null, response));
    }

    @PostMapping("/insert")
    ResponseEntity<?> insertProduct(@RequestBody ProductRequest request) {
        List<ProductResponse> foundProducts = productService.findByProductName(request.getProductName());
        return ResponseEntity.status(HttpStatus.OK).body(
                foundProducts.size() > 0 ?
                        new ResponseObject("Failed", "Product name already taken", "") :
                        new ResponseObject("Success", "Insert Product Successfully",
                                productService.save(request)));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@RequestBody ProductRequest request, @PathVariable("id") Long id) {
        request.setId(id);
        ProductResponse updatedProduct = productService.save(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Update Product Successfully", updatedProduct));
    }

    //delete
    @DeleteMapping("")
    ResponseEntity<?> deleteProduct(@RequestBody Long[] ids) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.delete(ids) ?
                new ResponseObject("ok", "Delete Product successfully", "") :
                new ResponseObject("failed", "Can not find product", ""));
    }
}
