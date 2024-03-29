package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.entities.Orders;
import com.cdweb.backend.payloads.requests.AttributeAndVariantsRequest;
import com.cdweb.backend.payloads.requests.PagesRequest;
import com.cdweb.backend.payloads.responses.*;
import com.cdweb.backend.services.IAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "attributeControllerOfAdmin")
@RequestMapping("/api/v1/admin/attribute")
@RequiredArgsConstructor
@Slf4j
public class AttributeController {
    private final IAttributeService attributeService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<AttributeResponse> response = attributeService.findByIsActiveTrue();
        return ResponseEntity.status(HttpStatus.OK).body(
                (response.size() > 0) ?
                        new ResponseObject("Success", null, response) :
                        new ResponseObject("Failed", "Have no attribute", null));
    }

    @PostMapping("/list-attributes")
    public ResponseEntity<?> getAllAttrAndVariants(@RequestBody PagesRequest request) {
        PageResponse<AttributeAndVariantsResponse> response = new PageResponse<>();
        response.setPage(request.getPage());
        Pageable pageable;
        if (request.getSort() != null){
            List<Order> orders = new ArrayList<>();
            request.getSort().forEach(e -> {
                orders.add(new Order(
                        e.getSortDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                        e.getSortBy()));
            });
            pageable = PageRequest.of(request.getPage() - 1, request.getLimit(), Sort.by(orders));
        } else {
            pageable =  PageRequest.of(request.getPage() - 1, request.getLimit(),
                    Sort.by(new Order(Sort.Direction.DESC, "modifiedDate")));
        }
        int totalItem = attributeService.totalItem();
        response.setTotalItems(totalItem);
        response.setTotalPages((int) Math.ceil((double) (totalItem) / request.getLimit()));
        response.setData(attributeService.findAllAttributeAndVariants(pageable));
        return response.getData().size() > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Success", null, response)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("Failed", "Have no attribute", null));
    }

    @GetMapping("/listVariants")
    public ResponseEntity<?> getAllAttrAndVariants() {
        List<AttributeAndVariantsResponse> response = attributeService.findAllAttributeAndVariants();
        return ResponseEntity.status(HttpStatus.OK).body(
                (response.size() > 0) ?
                        new ResponseObject("Success", null, response) :
                        new ResponseObject("Failed", "Have no attribute", null));
    }

    @PostMapping("")
    public ResponseEntity<?> insertAttributeAndVariants(@RequestBody AttributeAndVariantsRequest request) {
        AttributeAndVariantsResponse response = attributeService.save(request);
        return response == null ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("Failed", "Attribute name already taken", null)) :
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Success", "Insert Attribute Successfully", response));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAttribute(@RequestBody AttributeAndVariantsRequest request) {
        AttributeAndVariantsResponse response = attributeService.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                (response == null) ?
                        new ResponseObject("Failed", "Attribute name already taken", "") :
                        new ResponseObject("Success", "Insert Attribute Successfully", response));
    }

    @DeleteMapping("")
    ResponseEntity<?> deleteAttribute(@RequestBody Long[] ids) {
        return ResponseEntity.status(HttpStatus.OK).body(attributeService.delete(ids) ?
                new ResponseObject("Success", "Delete attribute successfully", true) :
                new ResponseObject("Failed", "Can not find brand", false));
    }

    @GetMapping("/exists/{id}/{attributeName}")
    ResponseEntity<?> existsAttributeByName(@PathVariable("id") Long id,
                                            @PathVariable("attributeName") String attributeName) {
        Boolean exists = attributeService.existsByNameAndIsActive(attributeName, id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "", exists));
    }
}
