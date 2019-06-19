import com.tdd.demo.FileStorage
import com.tdd.demo.pogos.Product
import spock.lang.Specification

class ProductsRepositorySpec extends Specification {

    FileStorage globalStorage

    def setup() {
        globalStorage = new FileStorage('/home/jresendiz/tienda.csv')
        globalStorage.ensureFile()
        globalStorage.addHeader(Product.csvHeader())
    }

    def "Should ensure csv file from Home"() {
        given:
        FileStorage fileStore = globalStorage
        expect:

        assert fileStore.ensureFile()
        assert fileStore.content() != null
    }

    def "Should add a product to the csv file"() {
        given:
        FileStorage fileStore = globalStorage
        expect:
        fileStore.addRow(new Product(cantidad: 10, sku: 'sku').toCSV())
        assert fileStore.content().split('\n')[0] =~ (Product.csvHeader(false))
        assert fileStore.content().length() > 0
    }

    def "Should get a list of products from the csv file"() {
        given:
        FileStorage fileStore = globalStorage
        and:
        // add products
        insertProducts(fileStore, 5)
        ArrayList<Product> products = fileStore.retrieveProductsFromCSV()
        expect:
        !products.isEmpty()
        products.size() == 5
        products.get(0) instanceof Product
    }

    def "Should Update a product to the csv file"() {
        given:
        FileStorage fileStore = globalStorage
        and:
        insertProducts(fileStore, 5)
        ArrayList<Product> original = fileStore.retrieveProductsFromCSV()
        original.get(0).cantidad = 1000
        fileStore.createCSVFromProductList(original)
        expect:
        ArrayList<Product> updated = fileStore.retrieveProductsFromCSV()
        assert original.get(0).toString() == updated.get(0).toString()
    }

    def "Should Delete a product to the csv file"() {
        given:
        FileStorage fileStore = globalStorage
        and:
        insertProducts(fileStore, 5)
        ArrayList<Product> original = fileStore.retrieveProductsFromCSV()
        ArrayList<Product> updated = fileStore.retrieveProductsFromCSV()
        updated.remove(0)
        fileStore.createCSVFromProductList(updated)
        expect:
        ArrayList<Product> retrieved = fileStore.retrieveProductsFromCSV()
        assert updated.size() == retrieved.size()
        assert original.size() != updated.size() && retrieved.size() != original.size()
    }

    def "Should find  a product to using the SKU"() {
        given:
        FileStorage fileStore = globalStorage
        and:
        insertProducts(fileStore, 5)
        ArrayList<Product> original = fileStore.retrieveProductsFromCSV()
        Product found = original.find { it.sku == 'sku-4' }
        expect:
        assert found
    }

    def cleanup() {
        globalStorage.storage.delete()
    }

    private insertProducts(FileStorage fileStorage, Integer quantity) {
        Random rand = new Random()
        (1..quantity).each { it ->
            fileStorage.addRow(new Product(
                    cantidad: rand.nextInt(),
                    precio: rand.nextFloat(),
                    marca: "marca-${rand.nextInt()}",
                    nombre: "nombre-${rand.nextInt()}",
                    sku: "sku-${it}").toCSV())
        }
    }

}
