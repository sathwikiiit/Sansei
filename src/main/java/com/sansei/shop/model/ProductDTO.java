package com.sansei.shop.model;

public class ProductDTO {
    private int id;
    private String name;
    private double price;
    private String description;
    private String category;
    private String tags;
    private int stock;
    public ProductDTO(Product testProduct) {
        this.id = testProduct.getId();
        this.name = testProduct.getName();
        this.category = testProduct.getCategory();
        this.price = testProduct.getPrice();
        this.stock = testProduct.getStock();
        this.tags = testProduct.getTags();
        this.description = testProduct.getDescription();
    }
    public ProductDTO() {
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
    public String getCategory() {
        return category;
    }
    public String getTags() {
        return tags;
    }
    public int getStock() {
        return stock;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

}
