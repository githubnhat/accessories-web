package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.payloads.requests.CategoryRequest;
import com.cdweb.backend.payloads.requests.PagesRequest;
import com.cdweb.backend.payloads.responses.*;
import com.cdweb.backend.payloads.responses.CategoryResponse;
import com.cdweb.backend.services.ICategoryService;
import com.cdweb.backend.services.impl.CategoryServiceImpl;
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

@RestController(value="categoriesControllerOfAdmin")
@RequestMapping("/api/v1/admin/category")
@RequiredArgsConstructor
@Slf4j
public class CategoriesController {

    private final ICategoryService categoryService;

    @PostMapping("/list")
    ResponseEntity<?> getAll(@RequestBody PagesRequest request){
        PageResponse<CategoryResponse> response = new PageResponse<>();
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
        int totalItem = categoryService.totalItem();
        response.setTotalItems(totalItem);
        response.setTotalPages((int) Math.ceil((double) (totalItem) / request.getLimit()));
        response.setData(categoryService.findAll(pageable));
        return response.getData().size() > 0
                ?
                ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Success", null, response))
                :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("Failed", "Have no category", null));
    }

    @GetMapping("")
    ResponseEntity<?> getAllIsActiveCategory(){
        List<CategoryResponse> response = categoryService.findByIsActiveTrue();
        return ResponseEntity.status(HttpStatus.OK).body(
                (response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Have no category", null));
    }

    @PostMapping("")
    ResponseEntity<?> insertCategory(@RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.save(request);
        return
                ResponseEntity.status(HttpStatus.OK).body((response != null)
                        ? new ResponseObject("Success", "Insert Category Successfully", response)
                        : new ResponseObject("Failed", "Category name already taken", null));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateCategory(@PathVariable("id") Long id,@RequestBody CategoryRequest request) {
        request.setId(id);
        CategoryResponse response = categoryService.save(request);
        return
                ResponseEntity.status(HttpStatus.OK).body((response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Failed", "Have no category", null));
    }

    @GetMapping("/exists/{code}")
    ResponseEntity<?> existsCategoryByCode(@PathVariable("code") String code) {
        log.info("pn {}", code);
        Boolean exists = categoryService.existsByCodeAndIsActiveTrue(code);
        log.info("kq {}", exists);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "", exists));
    }

    @DeleteMapping("")
    ResponseEntity<?> deleteCategory(@RequestBody Long[] ids) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.delete(ids) ?
                new ResponseObject("Success", "Delete category successfully", true) :
                new ResponseObject("Failed", "Can not find category", false));
    }
}

