package com.gfieast.qa;

import com.gfieast.qa.utils.Configuration;

public class DatabaseTest {

    protected String dbConnectionString;

    protected String connectionConfiguration() {
        dbConnectionString = Configuration.get("db_connection_string");
        return dbConnectionString;
    }

}
