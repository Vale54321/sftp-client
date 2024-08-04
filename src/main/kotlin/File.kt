package de.heiserer

import java.nio.file.Path
import java.nio.file.Paths

class SftpFile {
    private val path: Path

    constructor(pathString: String) {
        this.path = Paths.get(pathString)
    }

    constructor(vararg pathSegments: String) {
        this.path = Paths.get(pathSegments.joinToString("/"))
    }

    fun printPath(){
        println(path)
    }

    fun getPath(): String{
        return path.toString()
    }

    fun getName(): String{
        return path.fileName.toString()
    }
}