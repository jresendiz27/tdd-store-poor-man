package com.tdd.demo.dao

import com.tdd.demo.api.ProductRepository
import com.tdd.demo.pogos.Product
import com.tdd.demo.storage.DatabaseStorage
import groovy.sql.GroovyRowResult

class DatabaseProductDAO implements ProductRepository {
    DatabaseStorage databaseStorage

    DatabaseProductDAO(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage
    }

    Boolean createProduct(Product product) {
        databaseStorage.withTransaction('''
            insert into product(sku,cantidad,nombre,marca,precio) values ($sku,$cantidad,$nombre,$marca,$precio)
        ''', product.toMap())
    }

    Product findBySKU(String sku) {
        List results = databaseStorage.executeQuery('''
            select sku,cantidad,nombre,marca,precio from product where sku like '%$sku%'
        ''', [sku: sku])
        if (results) {
            return buildProductFromProperties(results.first())
        }
        return null
    }

    Boolean deleteBySKU(String sku) {
        databaseStorage.withTransaction('''
            delete from product where sku = $sku
        ''', [sku: sku])
    }

    Boolean updateProduct(String sku, Product product) {
        databaseStorage.withTransaction('''
            UPDATE product
            SET sku=$sku,cantidad=$cantidad,nombre=$nombre,marca=$marca,precio=$precio
            WHERE sku like'%$skuToSearch%\'; 
        ''', product.toMap() << [skuToSearch: sku])
    }

    List<Product> allProducts() {
        List results = databaseStorage.executeQuery('''
            select sku,cantidad,nombre,marca,precio from product''', [:])
        List<Product> products = []
        results.each { GroovyRowResult row ->
            products.add(buildProductFromProperties(row))
        }
        return products
    }

    private Product buildProductFromProperties(GroovyRowResult result) {
        new Product(result.collectEntries { [it.key, it.value] })
    }
}
