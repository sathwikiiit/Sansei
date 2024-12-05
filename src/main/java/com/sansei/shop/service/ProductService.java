package com.sansei.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sansei.shop.model.Product;
import com.sansei.shop.repository.ProductRepository;
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long item) {
        return productRepository.findById(item).orElse(null);
    }

    public Page<Product> getProducts(String category, String tags, String searchString, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;
    
        if (category != null && tags != null && searchString != null) {
            products = productRepository.findByCategoryAndTagsContainingIgnoreCaseAndNameContainingIgnoreCase(category, tags, searchString, pageable);
        } else if (category != null && searchString != null) {
            products = productRepository.findByCategoryAndNameContainingIgnoreCase(category, searchString, pageable);
        } else if (tags != null && searchString != null) {
            products = productRepository.findByTagsContainingIgnoreCaseAndNameContainingIgnoreCase(tags, searchString, pageable);
        } else if (category != null && tags != null) {
            products = productRepository.findByCategoryAndTagsContainingIgnoreCase(category, tags, pageable);
        } else if (category != null) {
            products = productRepository.findByCategory(category, pageable);
        } else if (tags != null) {
            products = productRepository.findByTagsContainingIgnoreCase(tags, pageable);
        } else if (searchString != null) {
            products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchString, searchString, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
    
        return products;
    }


}
