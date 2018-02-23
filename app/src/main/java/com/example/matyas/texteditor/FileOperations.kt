package com.example.matyas.texteditor

import android.os.Environment
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

object FileOperations {
    fun fileNameAvailable(name: String): Boolean {
        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/" + name + ".txt")
        return !file.exists()
    }

    fun saveFile(name: String, text: String) = try {
        val writer = FileWriter(Environment.getExternalStorageDirectory().absolutePath + "/" + name + ".txt")
        writer.write(text)
        writer.close()
        WriteSuccess()
    } catch (e: IOException) {
        Failure(e.toString())
    }

    fun loadFile(name: String) = try {
        val path = Environment.getExternalStorageDirectory().absolutePath + "/" + name + ".txt"
        val reader = FileReader(path)
        val text = reader.readText()
        reader.close()
        LoadSuccess(text)
    } catch (e: IOException) {
        Failure(e.toString())
    }

    fun getFiles(root: File = File(Environment.getExternalStorageDirectory().absolutePath)) : List<String> {
        val inFiles = mutableListOf<String>()
        val files = root.listFiles()

        files?.forEach {
            if (it.isDirectory) {
                inFiles.addAll(getFiles(it))
            } else {
                if (it.name.endsWith(".txt")) {
                    inFiles.add(it.nameWithoutExtension)
                }
            }
        }
        return inFiles.toList()
    }
}

sealed class FileOperationsResult
class WriteSuccess : FileOperationsResult()
class Failure(val error: String) : FileOperationsResult()
class LoadSuccess(val text: String) : FileOperationsResult()