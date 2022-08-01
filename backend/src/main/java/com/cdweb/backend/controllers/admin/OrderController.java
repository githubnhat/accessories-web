package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.responses.OrderResponse;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController(value="orderControllerOfAdmin")
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("/page/{page}/limit/{limit}")
    ResponseEntity<?> getOrdersPaging(@PathVariable("page") int page, @PathVariable("limit") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        int totalItem = orderService.totalOrder();

        PageResponse<OrderResponse> response = PageResponse.<OrderResponse>builder()
                .page(page)
                .totalPages((int) Math.ceil((double) (totalItem) / limit))
                .totalItems(totalItem)
                .data(orderService.getAllOrdersPaging(pageable))
                .build();

        return  ResponseEntity.status(HttpStatus.OK).body(
                (response.getData().size() > 0)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Have no order", null));
    }

    @PutMapping("")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequest orderRequest) {

        return null;
    }
}