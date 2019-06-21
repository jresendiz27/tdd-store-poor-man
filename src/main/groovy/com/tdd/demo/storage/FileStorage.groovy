package com.tdd.demo.storage

import com.tdd.demo.api.StorageAccess

class FileStorage implements StorageAccess {
    String path
    File resource

    FileStorage(String path) {
        this.path = path
    }

    boolean ensureResource() {
        this.ensureResource(path)
    }

    boolean ensureResource(String urlOrPath) {
        this.path = path ?: urlOrPath
        resource = new File(urlOrPath)
        if (!resource.exists()) {
            resource.createNewFile()
        }
        return true
    }

    String content() {
        return resource.text
    }

    void addHeader(String header, boolean withNewLine = false) {
        String currentContent = this.content()
        List<String> lines = currentContent.readLines()
        String headerWithNewLine = "$header${withNewLine ? '\n' : ''}"
        if (lines.empty || lines.first().trim().indexOf(header) > 0) {
            resource.write("$headerWithNewLine$currentContent")
        }
    }

    void addRow(String row, boolean withNewLine = false) {
        resource.append("${row}${withNewLine ? '\n' : ''}")
    }
}
