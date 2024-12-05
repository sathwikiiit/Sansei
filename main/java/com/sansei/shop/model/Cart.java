package com.sansei.shop.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApiUser  user;

    @ElementCollection
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    private Map<Long, Integer> items = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ApiUser getUser() {
        return user;
    }

    public void setUser(ApiUser user) {
        this.user = user;
    }

    public Map<Long, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Long, Integer> items) {
        this.items = items;
    }
    // Method to add or update product quantity
    public void addProduct(Long productId, Integer quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
    }

    // Method to remove a product from the cart
    public void removeProduct(Long productId) {
        if (items.containsKey(productId)) {
            if (items.get(productId) == 1) {
                items.remove(productId);
            } else {
                items.put(productId, items.get(productId) - 1);
            }
        }   
    }

}
