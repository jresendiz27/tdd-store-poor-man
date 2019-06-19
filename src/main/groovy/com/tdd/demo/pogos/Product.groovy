package com.tdd.demo.pogos

import groovy.transform.ToString

@ToString
class Product {

    String sku
    int cantidad
    String nombre
    String marca
    BigDecimal precio

    static String csvHeader(boolean withNewLine = true) {
        "sku,cantidad,nombre,marca,precio${withNewLine ? '\n' : ''}"
    }

    String toCSV(boolean withNewLine = true) {
        // TODO this might be dynamic ...
        $/"${sku ?: ''}",${cantidad ?: 0},"${nombre ?: ''}","${marca ?: ''}",${precio ?: 0.0}${
            withNewLine ? '\n' : ''
        }/$
    }

    void setCantidad(String cantidad) {
        this.cantidad = Integer.parseInt(cantidad)
    }

    void setPrecio(String precio) {
        this.precio = new BigDecimal(precio)
    }


}
