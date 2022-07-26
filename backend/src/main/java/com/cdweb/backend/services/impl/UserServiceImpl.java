package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.AddressConverter;
import com.cdweb.backend.converters.OrderConverter;
import com.cdweb.backend.converters.OrderItemConverter;
import com.cdweb.backend.entities.*;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.requests.UserRequest;
import com.cdweb.backend.payloads.responses.*;
import com.cdweb.backend.repositories.*;
import com.cdweb.backend.services.ICartItemService;
import com.cdweb.backend.services.IUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUsersService {
    private final UserRepository usersRepository;

    private final AddressRepository addressRepository;

    private final AddressConverter addressConverter;

    private final OrderRepository orderRepository;

    private final ProductCombinationRepository productCombinationRepository;

    private final OrderConverter orderConverter;

    private final OrderItemConverter orderItemConverter;

    private final ICartItemService cartItemService;

    private final OrderItemServiceImpl orderItemService;

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserResponse> response = new ArrayList<>();
        List<Users> entities = usersRepository.findByIsActiveTrue();
        for (Users user: entities) {
            response.add(UserResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .gmail(user.getGmail())
                    .role(user.getRoles().getRoleName())
                    .createdBy(user.getCreatedBy())
                    .createdDate(user.getCreatedDate())
                    .modifiedBy(user.getModifiedBy())
                    .modifiedDate(user.getModifiedDate())
                    .build());
        }
        return response;
    }

    @Override
    public AddressResponse findMainAddress(Users user) {
        Address address = addressRepository.findByUserAndIsMainAddressTrue(user);
        AddressResponse response = addressConverter.toResponse(address);
        return response;
    }

    @Override
    public List<AddressResponse> getAllAddresses(Users user) {
        List<Address> addresses = addressRepository.findByUserOrderByIsMainAddressDesc(user);
        List<AddressResponse> responses = new ArrayList<>();
        for (Address address: addresses) {
            AddressResponse response = addressConverter.toResponse(address);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public AddressResponse save(AddressRequest request, Users user) {
        if(request.getIsMainAddress() == true) {
            Address address = addressRepository.findByUserAndIsMainAddressTrue(user);
            if(address!=null){
                address.setIsMainAddress(false);
                addressRepository.save(address);
            }
        }
        Address address = addressRepository.save(Address.builder()
                        .address(request.getAddress())
                        .isMainAddress(request.getIsMainAddress())
                        .phone(request.getPhone())
                        .user(user)
                .build());
        AddressResponse response = addressConverter.toResponse(address);
        return response;
    }

    @Override
    public Boolean deleteAddress(Long id, Users user) {
        boolean exists = true;
        if (!addressRepository.existsById(id)) exists = false;
        addressRepository.deleteById(id);
        return exists;
    }

    @Override
    public List<OrderResponse> getOrders(Pageable pageable, Users user) {
        List<OrderResponse> responses = new ArrayList<>();
        List<Orders> entities = orderRepository.findByUserOrderByModifiedDateDesc(user, pageable).getContent();
        if (entities.size() > 0) {
            entities.forEach(entity -> {
                List<OrderItemResponse> orderItemResponses = orderItemService.findByOrderAndOrderByModifiedDateDesc(entity);
                OrderResponse response = orderConverter.toResponse(entity, orderItemResponses);
                responses.add(response);
            });
        }
        return responses;
    }

    @Override
    public OrderResponse save(OrderRequest orderRequest, Users user) {
        Orders order = orderConverter.toEntity(orderRequest);
        order.setStatus("Chờ xác nhận");
        order.setUser(user);
        Orders newEntity = orderRepository.save(order);
        List<OrderItemResponse> orderItems = orderItemService.saveOrderItemList(newEntity, orderRequest.getOrderItems());
        orderItems.forEach(orderItem -> {
            ProductCombinations productCombinations = productCombinationRepository
                    .findByProductIdAndProductVariantName(
                            orderItem.getProductId(),
                            orderItem.getProductCombination()
                    );
            productCombinations.setQuantity(productCombinations.getQuantity() - orderItem.getQuantity());
            productCombinationRepository.save(productCombinations);
        });
        return orderConverter.toResponse(newEntity, orderItems);
    }

    @Override
    public Boolean updateOrderStatus(Long orderId, String status) {
        Orders order = orderRepository.findById(orderId).orElseGet(null);
        if(order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    @Override
    public int totalOrder() {
        return (int) orderRepository.count();
    }

    @Override
    public int totalOrderByUser(Users user) {
        long result = orderRepository.countByUser(user);
        return (int) result;
    }

    @Override
    public int totalItem() {
        return (int) usersRepository.countByIsActiveTrue();
    }

    @Override
    public List<UserResponse> findAllAccountIsActiveTrue(Pageable pageable) {
        List<UserResponse> responses = new ArrayList<>();
        List<Users> userEntities = usersRepository.findByIsActiveTrue(pageable).getContent();
        if (!userEntities.isEmpty()){
            userEntities.forEach(u ->{
                responses.add(UserResponse.builder()
                        .id(u.getId())
                        .fullName(u.getFullName())
                        .username(u.getUsername())
                        .gmail(u.getGmail())
                        .role(u.getRoles().getRoleName())
                        .createdBy(u.getCreatedBy())
                        .createdDate(u.getCreatedDate())
                        .modifiedBy(u.getModifiedBy())
                        .modifiedDate(u.getModifiedDate())
                        .build());
            });
            return responses;
        } else {
            return null;
        }
    }


}
