package com.cdweb.backend.services.impl;

import com.cdweb.backend.converters.AddressConverter;
import com.cdweb.backend.converters.OrderConverter;
import com.cdweb.backend.converters.OrderItemConverter;
import com.cdweb.backend.converters.UserConverter;
import com.cdweb.backend.entities.*;
import com.cdweb.backend.payloads.requests.AddressRequest;
import com.cdweb.backend.payloads.requests.OrderRequest;
import com.cdweb.backend.payloads.requests.RegistrationRequest;
import com.cdweb.backend.payloads.requests.UserRequest;
import com.cdweb.backend.payloads.responses.*;
import com.cdweb.backend.repositories.*;
import com.cdweb.backend.services.ICartItemService;
import com.cdweb.backend.services.IUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final RoleRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserConverter userConverter;

    @Override
    public UserResponse getUser(Long id) {
        Users user = usersRepository.findByIdAndIsActiveTrue(id);
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .gmail(user.getGmail())
                .thumbnail(user.getThumbnail())
                .build();
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
        for (Address address : addresses) {
            AddressResponse response = addressConverter.toResponse(address);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public AddressResponse save(AddressRequest request, Users user) {
        if (request.getIsMainAddress() == true) {
            Address address = addressRepository.findByUserAndIsMainAddressTrue(user);
            if (address != null) {
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
        if (order != null) {
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
        if (!userEntities.isEmpty()) {
            userEntities.forEach(u -> {
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

    @Override
    public UserResponse update(RegistrationRequest request, Long id) {
        Users user = usersRepository.findByIdAndIsActiveTrue(id);
        if(user != null){
            user.setFullName(request.getFullName());
            user.setGmail(request.getGmail());
            user.setThumbnail(request.getThumbnail());
        }
        Users updatedUser = usersRepository.save(user);
        return UserResponse.builder()
                .thumbnail(updatedUser.getThumbnail())
                .username(updatedUser.getUsername())
                .fullName(updatedUser.getFullName())
                .gmail(updatedUser.getGmail())
                .build();
    }

    @Override
    public UserResponse update(UserRequest request) {
        Users user = usersRepository.findByIdAndIsActiveTrue(request.getId());
        if (user != null){
            user.setFullName(request.getFullName());
            user.setGmail(request.getGmail());
            Roles role = rolesRepository.findByRoleCode(request.getRoleCode());
            user.setRoles(role);
            Users updatedUser = usersRepository.save(user);
            return UserResponse.builder()
                    .gmail(updatedUser.getGmail())
                    .role(updatedUser.getRoles().getRoleName())
                    .fullName(updatedUser.getFullName())
                    .thumbnail(updatedUser.getThumbnail())
                    .username(updatedUser.getUsername())
                    .id(updatedUser.getId())
                    .build();
        }
        return null;
    }

    @Override
    public UserResponse saveNewAccount(UserRequest request) {
        if (usersRepository.findByUsernameAndIsActiveTrue(request.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }
        Roles role = rolesRepository.findByRoleCode(request.getRoleCode());
        //save
        Users userEntity = Users.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .gmail(request.getGmail())
                .thumbnail(request.getThumbnail())
                .roles(role)
                .isActive(true)
                .build();

        Users savedEntity = usersRepository.save(userEntity);
        return userConverter.toResponse(savedEntity);
    }

    @Override
    public boolean delete(Long[] ids, Long id) {
        boolean exists = true;
        for (Long i : ids) {
            if (i==id) {
                throw new IllegalArgumentException("Không thể xoá tài khoản của mình!");
            } else if (!usersRepository.existsByIdAndIsActiveTrue(i)) {
                exists = false;
            }
        }
        if (exists) {
            for (Long i : ids) {
                Users entity = usersRepository.findByIdAndIsActiveTrue(i);
                entity.setActive(false);
                usersRepository.save(entity);
            }
        }
        return exists;
    }


}
