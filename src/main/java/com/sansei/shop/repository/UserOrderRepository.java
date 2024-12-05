package com.sansei.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sansei.shop.model.ApiUser;
import com.sansei.shop.model.UserOrder;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {
    List<UserOrder> findByUser(ApiUser user);
    List<UserOrder> findAllByOrderStatus(String string);
}
