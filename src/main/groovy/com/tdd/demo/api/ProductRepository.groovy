package com.tdd.demo.api

import com.tdd.demo.pogos.Product

interface ProductRepository {
    Boolean createProduct(Product product)

    Product findBySKU(String sku)

    Boolean deleteBySKU(String sku)

    Boolean updateProduct(String sku, Product product)

    List<Product> allProducts()
}