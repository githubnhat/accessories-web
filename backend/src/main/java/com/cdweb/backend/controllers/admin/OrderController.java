package com.cdweb.backend.controllers.admin;

import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.requests.PagesRequest;
import com.cdweb.backend.payloads.responses.BrandResponse;
import com.cdweb.backend.payloads.responses.OrderResponse;
import com.cdweb.backend.payloads.responses.PageResponse;
import com.cdweb.backend.payloads.responses.ResponseObject;
import com.cdweb.backend.services.IOrderService;
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


@RestController(value="orderControllerOfAdmin")
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("list-orders")
    ResponseEntity<?> getOrdersPaging(@RequestBody PagesRequest request) {
        PageResponse<OrderResponse> response = new PageResponse<>();
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
        int totalItem = orderService.totalOrder();
        response.setTotalItems(totalItem);
        response.setTotalPages((int) Math.ceil((double) (totalItem) / request.getLimit()));
        response.setData(orderService.getAllOrdersPaging(pageable));
        return  ResponseEntity.status(HttpStatus.OK).body(
                (response.getData().size() > 0)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Have no order", null));
    }

    @GetMapping("/detail/{id}")
    ResponseEntity<?> getOrderDetail( @PathVariable("id") Long orderId) {
        OrderResponse response = orderService.getOrderDetail(orderId);
        return  ResponseEntity.status(HttpStatus.OK).body(
                (response != null)
                        ? new ResponseObject("Success", null, response)
                        : new ResponseObject("Success", "Not found order", null));
    }

    @PutMapping("")
    public ResponseEntity<?> updateOrder(@RequestBody OrderRequest orderRequest) {

        return null;
    }
}