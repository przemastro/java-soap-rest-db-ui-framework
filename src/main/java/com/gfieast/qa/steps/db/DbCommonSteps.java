package com.gfieast.qa.steps.db;

import com.gfieast.qa.DatabaseTest;
import com.gfieast.qa.models.QueryModel;
import com.gfieast.qa.utils.DataUtil;
import com.gfieast.qa.utils.QueryExecutor;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;
import java.sql.SQLException;

public class DbCommonSteps extends DatabaseTest {

    private QueryExecutor queryExecutor = new QueryExecutor();

    @Given("Ładuję dane testowe z pliku {string}")
    public void execQuery(String fileName) throws SQLException, IOException {
        final String query = DataUtil.readQueryFromFile(fileName);
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().execQuery(query);
    }

    @And("Ładuję dane testowe z pliku {string} z wartością wejściową {string}")
    public void execQueryWithParameter(String fileName, String parameterValue) throws SQLException, IOException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.parameterValue(parameterValue);

        final String query = DataUtil.readQueryFromFile(fileName);
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().execQueryWithParameter(query, queryBuilder.build());
    }

    @Given("Uruchomię procedurę {string} z pliku {string} z parametrem {string} i wartością {string}")
    public void execProcedureWithParameter(String procedureName, String fileName, String parameterName, String parameterValue) throws SQLException, IOException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.procedureName(procedureName);
        queryBuilder.parameterName(parameterName);
        queryBuilder.parameterValue(parameterValue);

        final String query = DataUtil.readQueryFromFile(fileName);
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().execProcedureWithParameter(query, queryBuilder.build());
    }

    @And("Uruchomię procedurę {string} z pliku {string}")
    public void executeProcedure(String procedureName, String fileName) throws SQLException, IOException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.procedureName(procedureName);

        final String query = DataUtil.readQueryFromFile(fileName);
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().execProcedure(query, queryBuilder.build());
    }

    @Given("Ilość rekordów w tabeli {string} wynosi {int}")
    public void countRowsInTable(String tableName, int expectedResult) throws SQLException, IOException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.tableName(tableName);

        final String query = DataUtil.readQueryFromFile("Scripts/countScript.sql");
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().countRowsInTable(query, expectedResult, queryBuilder.build());
    }

    @And("Ilość rekordów w tabeli {string} wynosi {int} gdy przefiltruję po kolumnie {string} wartością {string}")
    public void countRowsInTableWithAppliedFilter(String tableName, int expectedResult, String filterName, String filterValue) throws SQLException, IOException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.tableName(tableName);
        queryBuilder.filterName(filterName);
        queryBuilder.filterValue(filterValue);

        final String query = DataUtil.readQueryFromFile("countScriptWithParameter.sql");
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().countRowsInTableWithAppliedFilter(query, expectedResult, queryBuilder.build());
    }

    @And("Wartość w kolumnie {string} tabeli {string} jest równa {string} gdy przefiltruję po kolumnie {string} wartością {string}")
    public void validateValueInTableWithAppliedFilter(String columnName, String tableName, String expectedResult, String filterName, String filterValue) throws SQLException, IOException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.columnName(columnName);
        queryBuilder.tableName(tableName);
        queryBuilder.filterName(filterName);
        queryBuilder.filterValue(filterValue);

        final String query = DataUtil.readQueryFromFile("Scripts/validateDataWithFilter.sql");
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().validateValueInTableWithAppliedFilter(query, expectedResult, queryBuilder.build());
    }

    @Given("Wartość w kolumnie {string} tabeli {string} jest równa {string}")
    public void validateValueInTable(String columnName, String tableName, String expectedResult) throws SQLException, IOException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.columnName(columnName);
        queryBuilder.tableName(tableName);

        final String query = DataUtil.readQueryFromFile("Scripts/validateData.sql");
        queryExecutor = new com.gfieast.qa.utils.QueryExecutor().validateValueInTable(query, expectedResult, queryBuilder.build());
    }

    @And("Czyszczę bazę skryptem {string} z ustawionym filtrem {string}")
    public void deleteTestDataWithAppliedFilter(String fileName, String filterValue) throws SQLException, IOException {
        QueryModel queryModel = QueryModel.builder()
                .filterValue(filterValue)
                .build();

        final String query = DataUtil.readQueryFromFile(fileName);
        queryExecutor = new QueryExecutor().deleteTestDataWithAppliedFilter(query, queryModel);
    }

    @And("Czyszczę bazę po wykonaniu testu skryptem {string} z ustawionym filtrem {string}")
    public void deleteTestDataWithAppliedFilterAfterTest(String fileName, String filterValue) throws IOException, SQLException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.filterValue(filterValue);

        final String query = DataUtil.readQueryFromFile(fileName);
        queryExecutor.deleteTestDataWithAppliedFilter(query, queryBuilder.build());
    }


    @And("Czyszczę bazę przed rozpoczęciem testu skryptem {string} z ustawionym filtrem {string}")
    public void deleteTestDataWithAppliedFilterBeforeTest(String fileName, String filterValue) throws IOException, SQLException {
        QueryModel.QueryModelBuilder queryBuilder = QueryModel.builder();
        queryBuilder.filterValue(filterValue);

        final String query = DataUtil.readQueryFromFile(fileName);
        queryExecutor.deleteTestDataWithAppliedFilter(query, queryBuilder.build());
    }
}