package com.sansei.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sansei.shop.model.Product;
import com.sansei.shop.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.ok( productService.getProducts());
    }

    @GetMapping("/{pId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long pId){
        return ResponseEntity.ok(productService.getProduct(pId));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String searchString,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getProducts(category, tags, searchString, page, size));
    }

}
