package com.learn2gether.services;

import java.util.List;

import com.learn2gether.domain.Product;

/**
 * Created by jt on 1/26/16.
 */
public interface ProductService {

    Product getProduct(Integer id);

    List<Product> listProducts();
}
