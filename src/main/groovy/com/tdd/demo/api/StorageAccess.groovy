package com.tdd.demo.api

interface StorageAccess {
    boolean ensureResource()

    boolean ensureResource(String urlOrPath)
}