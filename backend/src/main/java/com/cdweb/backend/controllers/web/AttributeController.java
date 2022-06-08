package com.cdweb.backend.controllers.web;

import com.cdweb.backend.payloads.requests.AttributeRequest;
import com.cdweb.backend.payloads.responses.AttributeResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value ="attributeControllerOfWeb")
@RequestMapping("/api/v1/user/attribute")
@RequiredArgsConstructor
public class AttributeController {
    private final IAttributeService attributeService;

}
