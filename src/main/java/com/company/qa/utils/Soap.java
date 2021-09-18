package com.company.qa.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Soap {

    private static Response lastResponse;
    private static String lastRequestBody;

    private Soap() {
    }

    /**
     * Deduces type of passed content.
     *
     * @param body Request body content.
     * @return Content type.
     */
    private static String deduceContentType(String body) {
        return ContentType.XML.toString();
    }

    /**
     * Sends request to given endpoint.
     *
     * @param endpoint Endpoint to be called.
     * @param body     Body to be attached to request.
     */
    public static void request(String endpoint, String body) {
        final String requestUrl = Configuration.get("backend_url");

        lastRequestBody = body;

        RequestSpecification request = RestAssured.given();
        request.header("SOAPaction", endpoint);
        request.header("SSLClientCertSN", Configuration.getSSL());


        request.accept("*/*");
        request.body(body);
        request.auth().basic(Configuration.getLogin(), Configuration.getPassword());

        log.info("Sent POST request to " + requestUrl);
        if (!body.equals("")) {
            final String contentType = deduceContentType(body);
            request.contentType(contentType);
            log.info("Request body:\n" + body);
        }

        lastResponse = request.post(requestUrl);

        if (lastResponse != null) {
            log.info("Response body:\n" + lastResponse.asString());
        }
    }

    /**
     * Sends request to given endpoint.
     *
     * @param endpoint Endpoint to be called.
     */
    public static void request(String endpoint) {
        request(endpoint, "");
    }

    /**
     * Returns last HTTP response.
     *
     * @return lastResponse variable.
     */
    public static Response getLastResponse() {
        return lastResponse;
    }

    /**
     * Return last HTTP response body.
     *
     * @return lastResponse body as string.
     */
    public static String getLastResponseBody() {
        return lastResponse.getBody().asString();
    }

    /**
     * Returns last HTTP request body.
     *
     * @return lastRequestBody variable.
     */
    public static String getLastRequestBody() {
        return lastRequestBody;
    }
}