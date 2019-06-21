package com.tdd.demo.storage

import com.tdd.demo.api.StorageAccess
import groovy.sql.Sql

// TODO this can be migrated to a Pool Object (check disadvantages of singletons)
class DatabaseStorage implements StorageAccess {
    Sql connection
    boolean isConnected

    boolean ensureResource(String url) {
        url = url ?: System.getenv('MYSQL_CONNECTION') ?: 'jdbc://mysql:root@127.0.0.1:3306/productos'

        connection = Sql.newInstance(url, "com.mysql.jdbc.Driver")
        return isConnected = true
    }

    boolean ensureResource() {
        throw new RuntimeException('Not Required for this provider')
    }

    List executeQuery(String query, Map params) {
        return connection.rows(params, query)
    }

    boolean withTransaction(String query, Map params) {
        try {
            connection.withTransaction {
                connection.execute(params, query)
            }
        } catch (Exception e) {
            e.printStackTrace()
            return false
        }
        return true;
    }

    Sql getConnection() {
        throw new RuntimeException('Access denied to connection object')
    }

}