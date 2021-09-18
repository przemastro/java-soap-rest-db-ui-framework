package com.company.qa;

import com.company.qa.utils.Configuration;

public class DatabaseTest {

    protected String dbConnectionString;

    protected String connectionConfiguration() {
        dbConnectionString = Configuration.get("db_connection_string");
        return dbConnectionString;
    }

}
