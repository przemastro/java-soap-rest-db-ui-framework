package com.gfieast.qa.steps.api;

import com.gfieast.qa.ApiTest;
import com.gfieast.qa.utils.*;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.IOException;

public class ApiCommonSteps extends ApiTest {

    @After
    public void afterScenario(Scenario scenario) {
        afterClass(scenario);
    }

    @Given("Mam łączność z serwerem")
    public void connectionWithServer() {
        Rest.request("GET", "/");
        CheckUtil.checkStatusCode(Rest.getLastResponse(), 404);
    }

    @When("Wyślę zapytanie {string} na endpoint {string} bez ciała")
    public void sendRequestToEndpointWithoutBody(String method, String endpoint) {
        Rest.request(method, endpoint);
    }

    @Then("Otrzymam odpowiedź o kodzie statusu {string}")
    public void receiveResponseWithStatusCode(String statusCode) {
        CheckUtil.checkStatusCode(Rest.getLastResponse(), Integer.parseInt(statusCode));
    }

    @Then("Otrzymam odpowiedź o kodzie statusu <Status Code>")
    public void receiveResponseWithExampleStatusCode(int statusCode) {
        CheckUtil.checkStatusCode(Rest.getLastResponse(), statusCode);
    }

    @And("Otrzymam odpowiedź o ciele liczba całkowita")
    public void receiveResponseWithIntegerBody() {
        Assert.assertTrue(CheckUtil.isInteger(Rest.getLastResponseBody()));
    }

    @When("Wyślę zapytanie {string} na endpoint {string} z ciałem {string}")
    public void sendQueryToEndpointWithBody(String method, String endpoint, String body) throws IOException {
        body = DataUtil.parseFileEscapes(body);
        Rest.request(method, endpoint, body);
    }

    @And("Wyślę zapytanie {string} na endpoint {string} z ciałem z pliku {string}")
    public void sendRequestToEndpointWithBodyFromFile(String method, String endpoint, String readFilename) throws IOException {
        final String body = DataUtil.readTextFromFile("temp/" + readFilename);
        Rest.request(method, endpoint, body);
    }

    @And("Wyślę zapytanie {string} na endpoint {string} z ciałem {string} i zapiszę odpowiedź do pliku {string}")
    public void sendRequestToEndpointWithBodyAndSaveResponseToFile(String method, String endpoint, String body, String saveFilename) throws IOException {
        body = DataUtil.parseFileEscapes(body);
        Rest.request(method, endpoint, body);
        DataUtil.writeTextToTemporalFile(saveFilename, Rest.getLastResponseBody());
    }

    @And("Wyślę zapytanie na endpoint {string} z ciałem z pliku {string} i zapiszę odpowiedź do pliku {string}")
    public void sendRequestToEndpointWithBodyAndSaveResponseToFile(String endpoint, String readFilename, String saveFilename) throws IOException {
        final String body = DataUtil.readTextFromFile("temp/" + readFilename);
        Soap.request(endpoint, body);
        DataUtil.writeTextToTemporalFile(saveFilename, Soap.getLastResponseBody());
    }

    @And("Ciało będące liczbą całkowitą {int}")
    public void integerNumberBody(int expected) {
        CheckUtil.isInteger(Rest.getLastResponseBody());
        final int response = Integer.parseInt(Rest.getLastResponseBody());
        Assert.assertEquals(response, expected);
    }

    @And("Ciało będące liczbą rzeczywistą {double}")
    public void realNumberBody(double expected) {
        CheckUtil.isDouble(Rest.getLastResponseBody());
        final double response = Double.parseDouble(Rest.getLastResponseBody());
        Assert.assertEquals(response, expected);
    }

    @Then("Posprzątam katalog temp")
    public void clearTempDirectory() {
        DataUtil.clearTemporalDirectory();
    }

    @And("Wyślę zapytanie {string} na endpoint {string} z parametrami z pliku {string} i zapiszę odpowiedź do pliku {string}")
    public void sendRequestToEndpointWithParametersFromFile(String method, String endpoint, String readFilename, String writeFilename) throws IOException {
        final String parameters = DataUtil.readTextFromTemporalFile(readFilename);
        Rest.request(method, endpoint + "?" + parameters);
        DataUtil.writeTextToTemporalFile(writeFilename, Rest.getLastResponseBody());
    }

    @And("Wyślę zapytanie {string} na endpoint {string} z parametrami z pliku {string}")
    public void sendRequestToEndpointWithDefinedParametersFromFile(String method, String endpoint, String readFilename) throws IOException {
        final String parameters = DataUtil.readTextFromTemporalFile(readFilename);
        Rest.request(method, endpoint + "?" + parameters);
    }

    @And("Wyślę zapytanie {string} na endpoint {string} ze zdefiniowanym Id z pliku {string}")
    public void sendRequestToEndpointWithDefinedParameterFromFile(String method, String endpoint, String readFilename) throws IOException {
        final String parameters = DataUtil.readTextFromTemporalFile(readFilename);
        Rest.request(method, endpoint + parameters);
    }

    @When("Wyślę zapytanie {string} na adres z pliku {string} bez ciała")
    public void sendRequestToAddressFromFileWithoutBody(String method, String readFilename) throws IOException {
        final String address = DataUtil.readTextFromTemporalFile(readFilename);
        Rest.request(method, address);
    }

    @And("Ciało będące tekstem {string}")
    public void textBody(String expected) {
        Assert.assertEquals(Rest.getLastResponseBody(), expected);
    }

    @When("Przygotuję dane do usunięcia z pliku {string} i zapiszę do pliku {string}")
    public void prepareDataToDeletePaymentFromFileToFile(String readFilename, String saveFilename) throws IOException {
        final String text = DataUtil.readTextFromTemporalFile(readFilename);
        final JSONObject json = new JSONObject(text);
        DataUtil.writeTextToTemporalFile(saveFilename, json.get("id").toString());
    }

    @When("Modyfikuję pole {string} wartością {string} w pliku {string} i zapiszę do pliku {string}")
    public void modifyJsonFieldWithValue(String field, String value, String readFilename, String saveFilename) throws IOException {
        final String text = DataUtil.readTextFromTemporalFile(readFilename);
        final JSONObject json = new JSONObject(text);
        json.put(field,value);
        DataUtil.writeTextToTemporalFile(saveFilename, json.toString());
    }

    @And("Wyślę zapytanie {string} na endpoint {string} z ciałem {string} i zdefiniowanym Id")
    public void sendRequestWithDefinedBodyAndId(String method, String endpoint, String body) throws IOException {
        body = DataUtil.parseFileEscapes(body);
        final JSONObject json = new JSONObject(body);
        Rest.request(method, endpoint + json.get("id").toString(), body);
    }

    @Then("Sprawdzam czy wartość tagu {string} jest {string} dla pliku {string}")
    public void checkXmlTag(String tag, String expectedValue, String srcFile) throws IOException {
        final String body = DataUtil.readTextFromFile("temp/" + srcFile);
        CheckUtil.checkXmlValue(body, tag, expectedValue);
    }

    @When("Przygotuję ciało zapytania na bazie szablonu z pliku {string} i zapiszę do pliku {string}")
    public void prepareGetPaymentStatusReportRequestBody(String srcFile, String dstFile) throws IOException {
        String templateStr = DataUtil.readTextFromFile(Configuration.get("api_resources_path") + srcFile);
        DataUtil.writeTextToTemporalFile(dstFile, templateStr);
    }
}