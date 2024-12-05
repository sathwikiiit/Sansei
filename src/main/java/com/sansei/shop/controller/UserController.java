package com.sansei.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.sansei.shop.model.Address;
import com.sansei.shop.model.ApiUser;
import com.sansei.shop.model.Cart;
import com.sansei.shop.model.Product;
import com.sansei.shop.model.UserOrder;
import com.sansei.shop.repository.ProductRepository;
import com.sansei.shop.service.ApiUserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    private final ApiUserService apiUserService;
    private final ProductRepository productRepository;
    
    @Autowired
    public UserController(ApiUserService apiUserService , ProductRepository productRepository) {
        this.apiUserService = apiUserService;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<String> profile(HttpServletRequest request) {
        ApiUser  user = apiUserService.currentUser (request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied"); // Unauthorized if user not found
        }
        return ResponseEntity.ok(user.getUsername());
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> address(HttpServletRequest request) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        }
        return ResponseEntity.ok(user.getAddresses());
    }

    @PutMapping ("/address/add")
    public ResponseEntity<Address> addAddress(HttpServletRequest request, @RequestBody Address address) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        } else{
            user.setAddress(address);
            return ResponseEntity.ok(address);
        }
    }
    @PutMapping ("/address/remove/{index}")
    public ResponseEntity<String> removeAddress(HttpServletRequest request, @PathVariable int index) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( "Access denied"); // Unauthorized if user not found
        } else{
            user.removeAddress(index);
            return ResponseEntity.ok(" Address removed");
        }
    }

    @GetMapping ("/address/{index}")
    public ResponseEntity<Address> getAddress(HttpServletRequest request, @PathVariable int index) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        } else{
            user.removeAddress(index);
            return ResponseEntity.ok( user.getAddress(index));
        }
    }



    @GetMapping("/cart")
    public ResponseEntity<Cart> getCart(HttpServletRequest request) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        } else {
            Cart cart = apiUserService.getCart(request);
            return ResponseEntity.ok(cart);
        }
    }
        
    @PutMapping( "/cart/add/{pid}")
    public ResponseEntity<Product> addProductToCart(@PathVariable Long pid, HttpServletRequest request) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        } else {
            Product product = productRepository.findById(pid).orElse(null);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Not found if product not found
            } else {
                Cart cart= apiUserService.getCart(request);
                cart.addProduct(pid, 1);
                apiUserService.saveCart(request, cart);
                return ResponseEntity.ok(product);
            }
        }
    }

    @PutMapping("/cart/remove/{pid}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long pid, HttpServletRequest request) {

        ApiUser user = apiUserService.currentUser(request);
        Product product = productRepository.findById(pid).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in"); // Unauthorized if user not found
        } else if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"); // Not found if product not found
        } else {
            Cart cart= apiUserService.getCart(request);
            cart.removeProduct(pid);
            apiUserService.saveCart(request, cart);
            return ResponseEntity.ok("Product removed from cart");
        } 
    }

    @PostMapping ("/order")
    public ResponseEntity<String> placeOrder(HttpServletRequest request) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        } else {
            Cart cart = apiUserService.getCart(request);
            if (cart.getItems().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty");
            } else {
                return  ResponseEntity.ok(apiUserService.orderCart(request));
            }
        }
    }
    @GetMapping("/orders")
    public ResponseEntity<List<UserOrder>> getOrders(HttpServletRequest request) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        } else {
            return ResponseEntity.ok(apiUserService.getOrders(request));
        }
    }

    @GetMapping ("/orders/{oid}")
    public ResponseEntity<UserOrder> getOrder(@PathVariable Long oid, HttpServletRequest request) {
        ApiUser user = apiUserService.currentUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized if user not found
        } else{
            UserOrder order = apiUserService.getOrder(request, oid);
            if (order.getUser().equals( user)){
                return ResponseEntity.ok(apiUserService.getOrder(request, oid));
            } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
    }
}
