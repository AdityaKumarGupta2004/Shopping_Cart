package com.ecom.Shopping_cart.repository;

import com.ecom.Shopping_cart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
