package com.microservices.products.entities;

import com.sun.istack.NotNull;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @Column(name="product_name", unique=true)
    private String name;

    @Column(name="product_description")
    private String description;

    @Column(name="product_type")
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write="?::p_types")
    private ProductType productType;

    @Min(value=0, message="Product quantity cannot be less than zero")
    private Integer quantity;

    @NotNull
    @Min(value=0, message="Product price cannot be less than zero")
    @Column(name="price", precision=2)
    private BigDecimal price;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(Id, product.Id) && Objects.equals(name, product.name) && Objects.equals(description, product.description) && productType == product.productType && Objects.equals(quantity, product.quantity) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name, description, productType, quantity, price);
    }

    public enum ProductType {
        FOOD,
        SPORTS,
        HOUSEHOLD,
        MUSIC,
        ELECTRONIC,
        APPLIANCE
    }
}
