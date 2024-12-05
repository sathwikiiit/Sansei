package com.sansei.shop.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

@Entity
public class ApiUser implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( unique = true)
    private String username;
    private String password;
    private String email;
    private String phone;
    private String currentAdress;
    private String role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserOrder> orders;



    public ApiUser() {
    }

    public ApiUser(String username, String password, String email, String phone, String currentAdress) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.currentAdress = currentAdress;
        this.role="USER";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCurrentAddress() {
        return currentAdress;
    }

    public void setCurrentAddress(String currentAdress) {
        this.currentAdress = currentAdress;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()));
        return authorities;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Address getAddress(int index) {
        return addresses.get(index);
    }

    public void setAddress(Address address) {
        this.addresses.add(address);
    }
    public void removeAddress(int index) {
        this.addresses.remove(index);
    }

    public String getCurrentAdress() {
        return currentAdress;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
