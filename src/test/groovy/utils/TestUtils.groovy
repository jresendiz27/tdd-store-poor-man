package utils

import com.tdd.demo.api.ProductRepository
import com.tdd.demo.pogos.Product

import java.nio.file.Files

class TestUtils {
    static String creatTmpFile() {
        String tmpPath = Files.createTempDirectory("${System.currentTimeMillis()}").toString()
        return "$tmpPath/${UUID.randomUUID()}.txt"
    }

    static List<Map> createProductMapList(Integer quantity) {
        Random rand = new Random()
        List<Map> list = []
        (1..quantity).each {
            list.add([
                    cantidad: rand.nextInt(),
                    precio  : rand.nextFloat(),
                    marca   : "marca-${rand.nextInt()}",
                    nombre  : "nombre-${rand.nextInt()}",
                    sku     : "sku-${it}"
            ])
        }
        return list
    }

    static void insertProducts(ProductRepository productDao, Integer quantity) {
        createProductMapList(quantity).each { Map it ->
            productDao.createProduct(new Product().fromMap(it))
        }
    }
}
