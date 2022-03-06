package com.example.emptyproject

import java.io.File
import java.util.Date
import kotlin.collections.ArrayList

fun File.getDirectoryFiles(): ArrayList<FileObject> {
    val filesObjectArray = arrayListOf<FileObject>()
    for (file in listFiles()) {
        if (file.isDirectory) {
            continue
        }

        filesObjectArray.add(
            FileObject(
                name = file.name,
                date = Date(File(path, file.name).lastModified())
            )
        )
    }

    return filesObjectArray
}