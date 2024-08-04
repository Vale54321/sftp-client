package de.heiserer

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.io.File
import java.util.*

class SftpClient (
    private val sshHost: String,
    private val sshPort: Int = 22,
    private val username: String,
    private val password: String,
    private val baseDirectory: String = ""
) {
    private val jsch = JSch()
    private var session: Session? = null
    private var channelSftp: ChannelSftp? = null

    init {
        try {
            // Create a session
            session = jsch.getSession(username, sshHost, sshPort)
            session?.setPassword(password)

            // Avoid asking for key confirmation
            session?.setConfig("StrictHostKeyChecking", "no")
            session?.connect()

            // Create SFTP channel
            channelSftp = session?.openChannel("sftp") as ChannelSftp
            channelSftp?.connect()

            createDirectory(baseDirectory)

            channelSftp!!.cd(baseDirectory)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createDirectory(remoteDirectory: String) {
        try {
            val directories = remoteDirectory.split("/")
            var currentPath = channelSftp!!.pwd()

            for (dir in directories) {
                if (dir.isNotEmpty()) {
                    currentPath += "/$dir"
                    try {
                        channelSftp!!.ls(currentPath) // Check if the directory exists
                    } catch (e: Exception) {
                        println("MKDIRR $currentPath")
                        channelSftp!!.mkdir(currentPath) // Create the directory if it does not exist
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // Handle exceptions, consider logging instead
        }
    }

    fun uploadFile(file: File, directory: SftpFile) {
        if (file.exists()) {
            createDirectory(directory.getPath())

            val remoteFilePath = directory.getPath() + File.separator + file.name

            channelSftp!!.put(file.path, remoteFilePath)
        } else {
            println("File ${file.path} could not be created")
        }
    }

    fun uploadFileAndDelete(file: File, directory: SftpFile) {
        if (file.exists()) {
            createDirectory(directory.getPath())

            val remoteFilePath = directory.getPath() + File.separator + file.name

            channelSftp!!.put(file.path, remoteFilePath)

            file.delete()
        } else {
            println("File ${file.path} could not be created")
        }
    }

    fun listFiles(directory: SftpFile): List<SftpFile> {
        val fileList = mutableListOf<SftpFile>()

        val files = channelSftp!!.ls(directory.getPath()) as Vector<ChannelSftp.LsEntry>

        for (entry in files) {
            fileList.add(SftpFile(getAbsolutePath().getPath(), entry.filename))
        }

        fileList.forEach { file -> println(file.getPath()) }

        return fileList
    }

    fun getAbsolutePath(): SftpFile{
        return SftpFile(channelSftp!!.pwd())
    }

    fun changeDirectory(directory: SftpFile) {
        println("changing dir to: ${directory.getPath()}")
        channelSftp!!.cd(directory.getPath())
    }
}