package com.example.backend.model;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;
    @OneToOne
    private Product product;
    private double totalProductPrice;
    @ManyToOne
    private Order order;

    private int productQuantity;

    public OrderItem() {
    }

    public OrderItem(int orderItemId, Product product, double totalProductPrice, Order order, int productQuantity) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.totalProductPrice = totalProductPrice;
        this.order = order;
        this.productQuantity = productQuantity;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
