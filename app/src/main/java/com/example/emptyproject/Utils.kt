package com.example.emptyproject

import java.io.File
import java.util.Date
import kotlin.collections.ArrayList

class Utils {
    companion object {
        fun getDirectoryFiles(directory: File): ArrayList<FileObject> {
            val filesObjectArray = arrayListOf<FileObject>()
            for (file in directory.listFiles()) {
                if (file.isDirectory) {
                    continue
                }

                filesObjectArray.add(
                    FileObject(
                        name = file.name,
                        date = Date(File(directory.path, file.name).lastModified())
                    )
                )
            }

            return filesObjectArray
        }
    }
}