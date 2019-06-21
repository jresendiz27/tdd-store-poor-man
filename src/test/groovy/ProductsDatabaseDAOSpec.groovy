import com.tdd.demo.dao.DatabaseProductDAO
import com.tdd.demo.storage.DatabaseStorage
import groovy.sql.GroovyRowResult
import spock.lang.Specification
import utils.TestUtils

class ProductsDatabaseDAOSpec extends Specification {

    DatabaseProductDAO productDao

    def "Should get all products from db"() {
        given:
        def mock = GroovyMock(DatabaseStorage)
        1 * mock.executeQuery(_ as String, _ as Map) >> {
            def list = new ArrayList<GroovyRowResult>()
            TestUtils.createProductMapList(5).each {
                list.add(it as GroovyRowResult)
            }
            return list
        }
        productDao = new DatabaseProductDAO(mock)
        expect:
        !productDao.allProducts().empty
    }
}
