import de.heiserer.SftpClient
import de.heiserer.SftpFile
import org.junit.jupiter.api.Test
import java.io.File

class SftpClientTest {
    @Test
    fun testSftpClient() {
        val sftpClient = SftpClient(
            sshHost = "nas.heiserer.lan",
            sshPort =305,
            username = "timelapse",
            password = "E!46UZ!Ho7ac3gz!mvYv.wyz",
            baseDirectory = "test")

        val filename = "test.txt"
        val content = "test"

        val file = File(filename)
        file.writeText(content)

        sftpClient.uploadFile(file, SftpFile("bla"))

        val allFiles = sftpClient.listFiles(SftpFile("./"))
        allFiles.forEach {
            println(it)
        }

        file.delete()
    }

    @Test
    fun testFile(){
        val file = SftpFile("bla/bla1/bla2")
        file.printPath()

        val file2 = SftpFile("bla","bla1", "bla2")
        file2.printPath()
    }
}