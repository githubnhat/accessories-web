package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.converters.ProductConverter;
import com.cdweb.backend.payloads.requests.PagesRequest;
import com.cdweb.backend.payloads.requests.ProductRequest;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ProductResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IProductService;
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

@RestController(value = "productControllerOfAdmin")
@RequestMapping("/api/v1/admin/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final IProductService productService;

    private final ProductConverter productConverter;

    // this request is: http://localhost:8081/api/v1/products?page=1&limit=3
    @PostMapping("/list-products")
    ResponseEntity<?> showAllProduct(@RequestBody PagesRequest request) {
        PageResponse<ProductResponse> response = new PageResponse<>();
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
            // Sort.by(new Sort.Order(Sort.Direction.ASC, "brands_name")));
        }
        int totalItem = productService.totalItem();
        response.setTotalItems(totalItem);
        response.setTotalPages((int) Math.ceil((double) (totalItem) / request.getLimit()));
        response.setData(productService.findAllForAdmin(pageable));
        return response.getData().size() > 0 ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Success", null, response))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("Failed", "Have no product", null));
    }

    @PostMapping("/insert")
    ResponseEntity<?> insertProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.save(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                response == null ? new ResponseObject("Failed", "Product name already taken", "")
                        : new ResponseObject("Success", "Insert Product Successfully", response));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getProductDetails( @PathVariable("id") Long id) {
        ProductResponse response = productService.findByProductIdAdmin(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                response == null ? new ResponseObject("Failed", "Not found!", "")
                        : new ResponseObject("Success", null, response));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@RequestBody ProductRequest request, @PathVariable("id") Long id) {
        request.setId(id);
        ProductResponse updatedProduct = productService.save(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Update Product Successfully", updatedProduct));
    }

    // delete
    @DeleteMapping("")
    ResponseEntity<?> deleteProduct(@RequestBody Long[] ids) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.delete(ids) ? new ResponseObject("Success", "Delete Product successfully", true)
                        : new ResponseObject("Failed", "Can not find product", false));
    }

    @GetMapping("/exists/{productName}")
    ResponseEntity<?> existsProductByName(@PathVariable("productName") String productName) {
        Boolean exists = productService.existsByProductNameAndIsActive(productName);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "", exists));
    }
}
