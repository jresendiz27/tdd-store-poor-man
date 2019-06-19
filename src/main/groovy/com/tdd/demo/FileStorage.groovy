package com.tdd.demo

import com.tdd.demo.pogos.Product

class FileStorage {
    String path
    File storage

    public FileStorage(String path) {
        this.path = path
    }

    boolean ensureFile() {
        storage = new File(path)
        if (!storage.exists()) {
            storage.createNewFile()
        }
        return true
    }

    String content() {
        return storage.text
    }

    void addHeader(String header) {
        storage.append(header)
    }

    void addRow(String row) {
        storage.append(row)
    }


    ArrayList<Product> retrieveProductsFromCSV() {
        List<String> lines = storage.text.split('\n')
        List<String> properties = lines.get(0).split(',')
        List<Product> products = new ArrayList<>()
        lines.eachWithIndex { String line, int idx ->
            if (idx == 0) return
            List<String> values = line.split(',')
            Product product = new Product()
            properties.eachWithIndex { String entry, int i ->
                product[entry] = values.get(i).replaceAll("\"",'')
            }
            products.add(product)
        }
        return products
    }

    boolean createCSVFromProductList(List<Product> products, boolean force = true) {
        if (force) {
            storage.delete()
            this.ensureFile()
            this.addHeader(Product.csvHeader())
        }
        products.each { product ->
            this.addRow(product.toCSV())
        }
        return true
    }
}
