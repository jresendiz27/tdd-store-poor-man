import com.tdd.demo.dao.FileProductDAO
import com.tdd.demo.pogos.Product
import com.tdd.demo.storage.FileStorage
import spock.lang.Specification
import utils.TestUtils

class ProductsFileDAOSpec extends Specification {

    FileProductDAO productDao

    def setup() {
        productDao = new FileProductDAO()
        String tmpFile = TestUtils.creatTmpFile()
        productDao.storage = new FileStorage()
        productDao.storage.ensureResource(tmpFile)
    }

    def "Should add a product"() {
        given:
        productDao.createProduct(new Product([sku: 'un-id', precio: 2, marca: '3', cantidad: 1, nombre: '']))
        expect:
        productDao.storage.content() =~ 'sku'
        productDao.storage.content() =~ 'un-id'
    }


    def "Should get a list of products from the csv file"() {
        given:
        // add products
        TestUtils.insertProducts(productDao, 5)
        ArrayList<Product> products = productDao.allProducts()
        expect:
        !products.isEmpty()
        products.size() == 5
        products.get(0) instanceof Product
    }

    def "Should Update a product to the csv file"() {
        given:
        TestUtils.insertProducts(productDao, 5)
        ArrayList<Product> original = productDao.allProducts()
        productDao.updateProduct('sku-1', new Product(
                cantidad: 100,
                precio: 1000.0,
                marca: "lavida",
                nombre: "lavida",
                sku: "sku-lavida"
        ))
        expect:
        ArrayList<Product> updated = productDao.allProducts()

        original.size() == updated.size()
        updated.find { it.sku == "sku-lavida" }
    }

    def "Should Delete a product to the csv file"() {
        given:
        TestUtils.insertProducts(productDao, 5)
        ArrayList<Product> original = productDao.allProducts()
        productDao.deleteBySKU('sku-1')
        expect:
        ArrayList<Product> retrieved = productDao.allProducts()
        assert original.size() != retrieved.size()
    }

    def "Should find  a product to using the SKU"() {
        given:
        TestUtils.insertProducts(productDao, 5)
        expect:
        assert productDao.findBySKU('sku-3')
    }

    def "Should return false if cannot find product to delete"() {
        given:
        TestUtils.insertProducts(productDao, 5)
        expect:
        !productDao.deleteBySKU('la-vida-misma')
    }

    def "Should return false if cannot find product to update"() {
        given:
        TestUtils.insertProducts(productDao, 5)
        expect:
        !productDao.updateProduct('la-vida-misma', new Product())
    }


    def cleanup() {
        productDao.storage.resource.delete()
    }
}
