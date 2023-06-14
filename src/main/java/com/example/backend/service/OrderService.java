package com.example.backend.service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.model.*;
import com.example.backend.payload.OrderDto;
import com.example.backend.payload.OrderRequest;
import com.example.backend.payload.OrderResponse;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper mapper;

    //order Create method
    public OrderDto orderCreate(OrderRequest request, String username){
        User user = this.userRepository.findByemail(username).orElseThrow(() -> new ResourceNotFoundException("Username not found..."));
        int cartId = request.getCartId();
        String orderAddress = request.getOrderAddress();
        //find cart
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found..."));
        //getting cartItem
        Set<CartItem> items = cart.getItems();

        Order order = new Order();

        AtomicReference<Double> totalOrderPrice = new AtomicReference<Double>(0.0);
        Set<OrderItem> orderItems = items.stream().map((cartItem) -> {
            OrderItem orderItem = new OrderItem();
            //set cartItem to OrderItem
            //set product in orderItem
            orderItem.setProduct(cartItem.getProduct());

            //set ProductQty to orderItem
            orderItem.setProductQuantity(cartItem.getQuantity());

            orderItem.setTotalProductPrice(cartItem.getTotalPrice());

            orderItem.setOrder(order);

            totalOrderPrice.set(totalOrderPrice.get() + orderItem.getTotalProductPrice());

            int productId = orderItem.getProduct().getProduct_id();
            return orderItem;


        }).collect(Collectors.toSet());

        order.setBillingAddress(orderAddress);
        order.setOrderAmt(totalOrderPrice.get());
        order.setOrderDelivered(null);
        order.setOrderStatus("CREATED");
        order.setPaymentStatus("NOT PAID");
        order.setUser(user);
        order.setOrderItem(orderItems);
        order.setOrderCreated(new Date());

        Order save;
        if(order.getOrderAmt()>0){
            save = this.orderRepository.save(order);
            cart.getItems().clear();
            this.cartRepository.save(cart);
        }else {
            throw new ResourceNotFoundException("Add Cart First and Place order");
        }


        return this.mapper.map(save, OrderDto.class);
    }


    public void cancelOrder(int orderId){
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found..."));
        this.orderRepository.delete(order);

    }

    public OrderDto findOrderById(int orderId){
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order Not found"));
        return this.mapper.map(order, OrderDto.class);
    }

    //find all product by page
    public OrderResponse findAllOrders(int pageSize, int pageNumber, String sortDir, String sortBy){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> all = this.orderRepository.findAll(pageable);
        List<Order> content = all.getContent();
        //change order to orderDto
        List<OrderDto> collect = content.stream().map((each) -> this.mapper.map(each, OrderDto.class)).collect(Collectors.toList());

        OrderResponse response = new OrderResponse();
        response.setContent(collect);
        response.setPageNumber(all.getNumber());
        response.setPageSize(all.getSize());
        response.setLastPage(all.isLast());
        response.setTotalPage(all.getTotalPages());
        //getTotalElements return long
        response.setTotalElement(all.getTotalElements());


        return response;
    }

}
