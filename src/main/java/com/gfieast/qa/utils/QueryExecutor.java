package com.gfieast.qa.utils;

import com.gfieast.qa.DatabaseTest;
import com.gfieast.qa.models.QueryModel;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class QueryExecutor extends DatabaseTest {

    private static final String NEWLINE_REGEX = "[\r\n]+]";
    private List<HashMap<String, Object>> result;


    private void saveResult(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return;
        }
        final int columnNumber = resultSet.getMetaData().getColumnCount();
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= columnNumber; i++) {
            columnNames.add(resultSet.getMetaData().getColumnName(i));
        }

        result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new HashMap<>());
            for (int i = 1; i <= columnNumber; i++) {
                result.get(result.size() - 1)
                        .put(columnNames.get(i - 1), resultSet.getObject(i));
            }
        }
    }

    public List<HashMap<String, Object>> getResult() {
        return result;
    }

    private Connection createConnectionToDB() throws SQLException {
        dbConnectionString = connectionConfiguration();
        return DriverManager.getConnection(dbConnectionString);
    }

    public QueryExecutor execQuery(String query) throws SQLException {
        return execQuery(query, null);
    }

    private QueryExecutor execQuery(String query, List<String> parameters) throws SQLException {
        final String preparedQuery = query.replaceAll(NEWLINE_REGEX, " ");
        try (Connection con = createConnectionToDB(); PreparedStatement stmt = con.prepareStatement(preparedQuery)) {
            if (parameters != null) {
                for (int i = 0; i < parameters.size(); i++) {
                    stmt.setString(i + 1, parameters.get(i));
                }
            }
            stmt.execute();
            saveResult(stmt.getResultSet());
        }
        return this;
    }

    public QueryExecutor execQueryWithParameter(String query, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getParameterValue());
        return execQuery(query, parameters);
    }

    public QueryExecutor execProcedure(String query, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getProcedureName());
        return execQuery(query, parameters);
    }

    public QueryExecutor execProcedureWithParameter(String query, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getProcedureName());
        parameters.add(queryModel.getParameterName());
        parameters.add(queryModel.getParameterValue());
        return execQuery(query, parameters);
    }

    public QueryExecutor countRowsInTable(String query, int expectedResult, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getTableName());
        execQuery(query, parameters);

        final int actualResult = (int) result.get(0).get("");
        Assert.assertEquals(actualResult, expectedResult);
        return this;
    }

    public QueryExecutor countRowsInTableWithAppliedFilter(String query, int expectedResult, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getTableName());
        parameters.add(queryModel.getFilterName());
        parameters.add(queryModel.getFilterValue());
        execQuery(query, parameters);

        final int actualResult = (int) result.get(0).get("");
        Assert.assertEquals(actualResult, expectedResult);
        return this;
    }

    public QueryExecutor validateValueInTable(String query, String expectedResult, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getColumnName());
        parameters.add(queryModel.getTableName());
        execQuery(query, parameters);

        final String actualResult = result.get(0).get(queryModel.getColumnName()).toString();
        Assert.assertEquals(actualResult, expectedResult);
        return this;
    }

    public QueryExecutor validateValueInTableWithAppliedFilter(String query, String expectedResult, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        parameters.add(queryModel.getColumnName());
        parameters.add(queryModel.getTableName());
        parameters.add(queryModel.getFilterName());
        parameters.add(queryModel.getFilterValue());
        execQuery(query, parameters);

        final String actualResult = result.get(0).get(queryModel.getColumnName()).toString();
        Assert.assertEquals(actualResult, expectedResult);
        return this;
    }

    public QueryExecutor deleteTestDataWithAppliedFilter(String query, QueryModel queryModel) throws SQLException {
        List<String> parameters = new ArrayList<>();
        final String[] filters = queryModel.getFilterValue().split(",");
        Collections.addAll(parameters, filters);

        return execQuery(query, parameters);
    }
}
