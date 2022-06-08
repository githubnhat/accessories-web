package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.requests.VariantRequest;
import com.cdweb.backend.payloads.responses.AttributeResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.payloads.responses.VariantResponse;
import com.cdweb.backend.services.IAttributeService;
import com.cdweb.backend.services.IVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value ="attributeControllerOfAdmin")
@RequestMapping("/api/v1/admin/attribute")
@RequiredArgsConstructor
public class AttributeController {
    private final IAttributeService attributeService;

    @GetMapping("")
    public ResponseEntity<?> getAll(){
        List<AttributeResponse> response = attributeService.findByIsActiveTrue();
        return ResponseEntity.status(HttpStatus.OK).body(
                (response.size()>0) ?
                        new ResponseObject("Success", null, response) :
                        new ResponseObject("Failed", "Have no attribute", ""));
    }

    @PostMapping("")
    public ResponseEntity<?> insertVariants(@RequestBody AttributeRequest request){
        AttributeResponse response = attributeService.save(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                (response==null) ?
                        new ResponseObject("Failed", "Attribute name already taken", "") :
                        new ResponseObject("Success", "Insert Attribute Successfully", response));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVariants(@RequestBody AttributeRequest request, @PathVariable("id") Long id){
        request.setId(id);
        AttributeResponse response = attributeService.save(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                (response==null) ?
                        new ResponseObject("Failed", "Attribute name already taken", "") :
                        new ResponseObject("Success", "Insert Attribute Successfully", response));
    }
}
