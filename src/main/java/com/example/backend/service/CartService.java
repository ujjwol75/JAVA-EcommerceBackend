package com.example.backend.service;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.payload.CartDto;
import com.example.backend.payload.ItemRequest;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
   private UserRepository userRepository;

    @Autowired
   private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;



    public CartDto addItem(ItemRequest item, String username){

        int productId = item.getProductId();
        int quantity = item.getQuantity();

        //fetch user
        User user = this.userRepository.findByemail(username).orElseThrow(() -> new ResourceNotFoundException("User not found..."));
      //fetch product
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found..."));
        //check product is in stock or not
        if(!product.isStock()){
            new ResourceNotFoundException("Product Out Of Stock!");
        }

        //create cartItem with productId and Quantity
        CartItem cartItem = new CartItem();
        System.out.println("product in cart:: "+ product);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        double totalPrice = product.getProduct_price()*quantity;
        cartItem.setTotalPrice(totalPrice);

        //getting cart from user
        Cart cart = user.getCart();

        if(cart == null){
            cart= new Cart();
            user.setCart(cart);
        }

        cartItem.setCart(cart);
        Set<CartItem> items = cart.getItems();

        System.out.println("items maa k xa? :: "+ items);

        //we checking product which is already availabel in CartItem or not
        //if product is available then increase its quantity by 1
        System.out.println("check check");
        AtomicReference<Boolean> flag = new AtomicReference<>(false); //boolean flag; banako jastai ho tara yesto garyo vaney lambda function vitra varibale access hudaina so AtomicRefrence liyera gareko
        System.out.println("check check");
        Set<CartItem> newProduct = items.stream().map(i -> {
            if (i.getProduct().getProduct_id() == product.getProduct_id()) {
                i.setQuantity(quantity);
                i.setTotalPrice(totalPrice);
                flag.set(true);
            }
            return i;
        }).collect(Collectors.toSet());

        if(flag.get()){
            items.clear();
            items.addAll(newProduct);
        }else {
            cartItem.setCart(cart);
            items.add(cartItem);
        }
        cart.setUser(user);

        Cart saveCart = this.cartRepository.save(cart);

        return this.mapper.map(saveCart, CartDto.class);
    }


    public CartDto getCartAll(String email){
        //find user
        User user = this.userRepository.findByemail(email).orElseThrow(() -> new ResourceNotFoundException("Username not found..."));
        System.out.println("user in cart:: "+ user.getName());
        //find cart
        Cart cart = this.cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("There is no cart"));



        return this.mapper.map(cart, CartDto.class);
    }

    //get cart by CartId
    public CartDto getCartById(int cartId, String username){
        User user = this.userRepository.findByemail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart findByUserandcartId = this.cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart Id not found"));

        return this.mapper.map(findByUserandcartId, CartDto.class);
    }

    //remove cartItem
    public CartDto removeCartItemFromCart(String username, int productId){
        User user = this.userRepository.findByemail(username).orElseThrow(() -> new ResourceNotFoundException());
        Cart cart = user.getCart();
        Set<CartItem> items = cart.getItems();
        boolean removeIf = items.removeIf((i) -> i.getProduct().getProduct_id() == productId);
        Cart save = this.cartRepository.save(cart);
        System.out.println("removeIf:: "+ removeIf);
        return this.mapper.map(save, CartDto.class);
    }



}
