package com.tdd.demo.dao

import com.tdd.demo.api.ProductRepository
import com.tdd.demo.pogos.Product
import com.tdd.demo.storage.FileStorage

class FileProductDAO implements ProductRepository {
    FileStorage storage

    @Override
    Boolean createProduct(Product product) {
        storage.addHeader(product.csvHeader())
        storage.addRow(product.toCSV())
    }

    @Override
    Product findBySKU(String sku) {
        List<Product> products = this.allProducts()
        return products.find { it.sku.trim() == sku }
    }

    @Override
    Boolean deleteBySKU(String sku) {
        List<Product> products = this.allProducts()
        Integer indexOfProduct = products.findIndexOf { it.sku.trim() == sku }
        if (indexOfProduct >= 0) {
            products.remove(indexOfProduct)
            return createCSVFromProductList(products)
        }
        return false
    }

    @Override
    Boolean updateProduct(String sku, Product product) {
        List<Product> products = this.allProducts()
        Integer indexOfProduct = products.findIndexOf { it.sku.trim() == sku }
        if (indexOfProduct >= 0) {
            products.set(indexOfProduct, product)
            return createCSVFromProductList(products)
        }
        return false
    }

    boolean createCSVFromProductList(List<Product> products, boolean force = true) {
        if (force) {
            storage.resource.delete()
            storage.ensureResource()
            storage.addHeader(Product.csvHeader())
        }
        products.each { product ->
            storage.addRow(product.toCSV())
        }
        return true
    }

    @Override
    List<Product> allProducts() {
        List<String> lines = storage.content().split('\n')
        List<String> properties = lines.get(0).split(',')
        List<Product> products = new ArrayList<>()
        lines.eachWithIndex { String line, int idx ->
            if (idx == 0) return
            List<String> values = line.split(',')
            Product product = new Product()
            properties.eachWithIndex { String entry, int i ->
                product[entry] = values.get(i).replaceAll("\"", '')
            }
            products.add(product)
        }
        return products

    }
}
