package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.payloads.requests.CategoryRequest;
import com.cdweb.backend.payloads.responses.CategoryResponse;
import com.cdweb.backend.payloads.responses.CategoryResponse;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/category")
@RequiredArgsConstructor
@Slf4j
public class CategoriesController {

    private final CategoryServiceImpl categoryService;

    @GetMapping("/page/{page}/limit/{limit}")
    ResponseEntity<?> getAll(@PathVariable("page") int page, @PathVariable("limit") int limit){
        Pageable pageable = PageRequest.of(page - 1, limit);
        PageResponse<CategoryResponse> response = PageResponse.<CategoryResponse>builder()
                .page(page)
                .totalPage((int) Math.ceil((double) (categoryService.totalItem()) / limit))
                .data(categoryService.findAll(pageable))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(
                (response.getData() != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Have no category", null));
    }

    @PostMapping("")
    ResponseEntity<?> insertCategory(@RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.save(request);
        return
                ResponseEntity.status(HttpStatus.OK).body((response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Failed", "Category name already taken", null));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateCategory(@PathVariable("id") Long id,@RequestBody CategoryRequest request) {
        request.setId(id);
        CategoryResponse response = categoryService.save(request);
        return
                ResponseEntity.status(HttpStatus.OK).body((response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Failed", "Category name already taken", null));
    }
}
