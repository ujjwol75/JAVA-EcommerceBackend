package com.example.backend.controller;

import com.example.backend.model.Order;
import com.example.backend.payload.ApiResponse;
import com.example.backend.payload.OrderDto;
import com.example.backend.payload.OrderRequest;
import com.example.backend.payload.OrderResponse;
import com.example.backend.service.OrderService;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequest orderRequest, Principal principal){
        String username = principal.getName();
        OrderDto orderDto = this.orderService.orderCreate(orderRequest, username);
        return new ResponseEntity<OrderDto>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrderById(@PathVariable int orderId){
        this.orderService.cancelOrder(orderId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Order Deleted....s", true), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable int orderId){
        OrderDto orderById = this.orderService.findOrderById(orderId);
        return new ResponseEntity<OrderDto>(orderById, HttpStatus.ACCEPTED);
    }

    @GetMapping("/findall")
    public OrderResponse findAllOrder(@RequestParam(defaultValue = "2", value = "pageSize") int pageSize,
                                      @RequestParam(defaultValue = "0", value = "pageNumber") int pageNumber)
    {
        OrderResponse allOrders = this.orderService.findAllOrders(pageSize, pageNumber, null, null);
        return allOrders;
    }
    
    
}
