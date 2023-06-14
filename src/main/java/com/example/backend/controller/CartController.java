package com.example.backend.controller;

import com.example.backend.payload.CartDto;
import com.example.backend.payload.ItemRequest;
import com.example.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/")
    public ResponseEntity<CartDto> addToCart(@RequestBody ItemRequest itemRequest, Principal principal){
        System.out.println("principal :: "+ principal.getName());
        CartDto addItem = this.cartService.addItem(itemRequest, principal.getName());
        return new ResponseEntity<CartDto>(addItem, HttpStatus.OK);
    }


    @GetMapping("/")
    public ResponseEntity<CartDto> getAllCart(Principal principal){
        CartDto cartAll = this.cartService.getCartAll(principal.getName());
        return new ResponseEntity<CartDto>(cartAll, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable int cartId, Principal principal){
        System.out.println("cartId:::: "+ cartId);
        CartDto cartById = this.cartService.getCartById(cartId, principal.getName());
        return new ResponseEntity<CartDto>(cartById, HttpStatus.OK);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<CartDto> deleteCartItemFromCart(@PathVariable int pid, Principal principal){
        CartDto cartDto = this.cartService.removeCartItemFromCart(principal.getName(), pid );
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.UPGRADE_REQUIRED);
    }


}
