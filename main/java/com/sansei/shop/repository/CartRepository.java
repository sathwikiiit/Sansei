package com.sansei.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sansei.shop.model.ApiUser;
import com.sansei.shop.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUser(ApiUser user);
    
}