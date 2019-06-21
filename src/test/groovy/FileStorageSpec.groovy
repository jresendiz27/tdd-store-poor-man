import com.tdd.demo.storage.FileStorage
import spock.lang.Specification
import utils.TestUtils

class FileStorageSpec extends Specification {
    FileStorage fileStorage

    def setup() {
        String tmpFilePath = TestUtils.creatTmpFile()
        fileStorage = new FileStorage(tmpFilePath)
    }

    def "Should create a file"() {
        given:
        fileStorage.ensureResource()
        expect:
        fileStorage.resource.exists()
    }

    def "Should append information to a file"() {
        given:
        fileStorage.ensureResource()
        fileStorage.addHeader('Header')
        fileStorage.addRow('Something')
        expect:
        fileStorage.resource.exists()
        fileStorage.resource.text =~ 'Header'
        fileStorage.resource.text =~ 'Something'
    }

    def cleanup() {
        fileStorage.resource.delete()
    }

}
