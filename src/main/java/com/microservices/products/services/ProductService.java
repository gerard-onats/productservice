package com.microservices.products.services;

import com.microservices.products.entities.Product;
import com.microservices.products.exceptions.ProductNotFoundException;
import com.microservices.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product does not exist."));
    }

    public Product addProduct(Product p) {
        return productRepository.save(p);
    }

    public Product updateProduct(Long id, Product product) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product does not exist."));

        if(product.getName() != null) p.setName(product.getName());
        if(product.getDescription() != null) p.setDescription(product.getDescription());
        if(product.getProductType() != null) p.setProductType(product.getProductType());
        if(product.getQuantity() != null) p.setQuantity(product.getQuantity());
        if(product.getPrice() != null) p.setPrice(product.getPrice());

        return productRepository.save(p);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
