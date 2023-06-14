package com.example.backend.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    //Relationship with other tables

    @OneToMany(mappedBy = "cart" ,cascade= CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    @OneToOne
    private User user;

    public Cart(int cartId, Set<CartItem> items, User user) {
        this.cartId = cartId;
        this.items = items;
        this.user = user;
    }

    public Cart() {
    }

    public Set<CartItem> getItems() {
        return items;
    }

    public void setItems(Set<CartItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cart(int cartId) {
        this.cartId = cartId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
