package com.gfieast.qa.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Rest {

    private static Response lastResponse;
    private static String lastRequestBody;

    private Rest() {
    }

    /**
     * Deduces type of passed content. TODO
     *
     * @param body Request body content.
     * @return Content type.
     */
    private static String deduceContentType(String body) {
        return ContentType.JSON.toString();
    }

    /**
     * Sends request to given endpoint.
     *
     * @param method   HTTP method to be used.
     * @param endpoint Endpoint to be called.
     * @param body     Body to be attached to request.
     */
    public static void request(String method, String endpoint, String body) {
        final String requestUrl = Configuration.get("backend_url") + endpoint;

        lastRequestBody = body;

        RequestSpecification request = RestAssured.given();
        request.accept("*/*");
        request.body(body);

        log.info("Sent " + method + " request to " + requestUrl);
        if (!body.equals("")) {
            final String contentType = deduceContentType(body);
            request.contentType(contentType);
            log.info("Request body:\n" + body);
        }

        switch (method.toUpperCase()) {
            case "GET":
                lastResponse = request.get(requestUrl);
                break;
            case "HEAD":
                lastResponse = request.head(requestUrl);
                break;
            case "POST":
                lastResponse = request.post(requestUrl);
                break;
            case "PUT":
                lastResponse = request.put(requestUrl);
                break;
            case "DELETE":
                lastResponse = request.delete(requestUrl);
                break;
            case "OPTIONS":
                lastResponse = request.options(requestUrl);
                break;
            case "PATCH":
                lastResponse = request.patch(requestUrl);
                break;
            default:
                lastResponse = null;
        }

        if (lastResponse != null) {
            log.info("Response body:\n" + lastResponse.asString());
        }
    }

    /**
     * Sends request to given endpoint.
     *
     * @param method   HTTP method to be used.
     * @param endpoint Endpoint to be called.
     */
    public static void request(String method, String endpoint) {
        request(method, endpoint, "");
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