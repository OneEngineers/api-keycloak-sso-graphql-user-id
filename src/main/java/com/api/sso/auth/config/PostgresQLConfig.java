package com.api.sso.auth.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class PostgresQLConfig {
    public void TestRun() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/api_sso_auth";
        String user = "api_sso_user";
        String pass = "api_sso_password";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Driver: " + meta.getDriverName());
            System.out.println("Version: " + meta.getDatabaseProductVersion());
        }
    }
}
