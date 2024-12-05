package com.sansei.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sansei.shop.model.Product;
import com.sansei.shop.model.ProductDTO;
import com.sansei.shop.model.UserOrder;
import com.sansei.shop.repository.ProductRepository;
import com.sansei.shop.repository.UserOrderRepository;

@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final UserOrderRepository userOrderRepository;
        
    @Autowired
    public AdminService(ProductRepository productRepository, UserOrderRepository userOrderRepository) {
        this.productRepository = productRepository;
        this.userOrderRepository = userOrderRepository;
    }

    public void addProduct(ProductDTO product) {
        Product productEntity = new Product();
        productEntity.setName(product.getName());
        productEntity.setPrice(product.getPrice());
        productEntity.setDescription(product.getDescription());
        productEntity.setCategory(product.getCategory());
        productEntity.setStock(product.getStock());
        productEntity.setTags(product.getTags());
        productRepository.save(productEntity);
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public void updateProduct(Long Id,ProductDTO product) {
        Product productEntity = productRepository.findById(Id).orElse(null);
        if (productEntity == null) {
            return;
        }
        if (product.getName() != null) {
            productEntity.setName(product.getName());
        } else if (product.getDescription() != null) {
            productEntity.setDescription(product.getDescription());
        } else if (product.getPrice() != 0) {
            productEntity.setPrice(product.getPrice());
        } else if (product.getStock() != 0) {
            productEntity.setStock(product.getStock());
        }
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void restockProduct(Long pid, int quantity) {
        Product product = productRepository.findById(pid).orElse(null);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public List<UserOrder> getPendingOrders() {
        return userOrderRepository.findAllByOrderStatus("pending");
    }

    public void confirmOrder(Long oid, String date) {
        UserOrder order = userOrderRepository.findById(oid).orElse(null);
        if (order != null) {
            order.setOrderStatus("confirmed");
            order.setDeliveryDate(date);
            userOrderRepository.save(order);
        } else{
            throw new RuntimeException("Order not found");
        }
    }
    
}
