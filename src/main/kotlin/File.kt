package de.heiserer

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class SftpFile {
    private val path: Path

    constructor(pathString: String) {
        this.path = Paths.get(pathString)
    }

    constructor(vararg pathSegments: String) {
        this.path = Paths.get(pathSegments.joinToString(File.separator))
    }

    fun printPath(){
        println(path)
    }

    fun getPath(): String{
        // convert path to unix path for every os
        return path.toString().replace("\\", "/")
    }

    fun getName(): String{
        return path.fileName.toString()
    }
}