package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.common.ErrorResponse;
import com.cdweb.backend.payloads.requests.PagesRequest;
import com.cdweb.backend.payloads.responses.AttributeAndVariantsResponse;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.payloads.responses.UserResponse;
import com.cdweb.backend.services.IUsersService;
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


@RestController
@RequestMapping("/api/v1/admin/account")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final IUsersService usersService;

    @PostMapping("/list-accounts")
    public ResponseEntity<?> getListAccount(@RequestBody PagesRequest request){
        PageResponse<UserResponse> response = new PageResponse<>();
        response.setPage(request.getPage());
        Pageable pageable;
        if (request.getSort() != null){
            List<Sort.Order> orders = new ArrayList<>();
            request.getSort().forEach(e -> {
                orders.add(new Sort.Order(
                        e.getSortDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                        e.getSortBy()));
            });
            pageable = PageRequest.of(request.getPage() - 1, request.getLimit(), Sort.by(orders));
        } else {
            pageable =  PageRequest.of(request.getPage() - 1, request.getLimit(),
                    Sort.by(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        }
        int totalItem = usersService.totalItem();
        response.setTotalItems(totalItem);
        response.setTotalPages((int) Math.ceil((double) (totalItem) / request.getLimit()));
        response.setData(usersService.findAllAccountIsActiveTrue(pageable));
        return response.getData().size()>0 ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Success", null, response)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("Failed", "Have no attribute", null)) ;
    }

}

