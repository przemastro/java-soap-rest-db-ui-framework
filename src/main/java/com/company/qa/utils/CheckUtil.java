package com.company.qa.utils;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class CheckUtil {

    private CheckUtil() {
    }

    /**
     * Checks if given string represents integer.
     *
     * @param str String to be checked..
     * @return True if given string represents integer, else false.
     */
    public static boolean isInteger(String str) {
        log.info("Checking if integer");
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if given string represents double.
     *
     * @param str String to be checked.
     * @return True if given string represents double, else false.
     */
    public static boolean isDouble(String str) {
        log.info("Checking if double");
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }


    /**
     * Checks if given string is an valid JSON.
     *
     * @param text String to be checked.
     * @return True if given string is valid JSON, else false.
     */
    public static boolean isJSONValid(String text) {
        log.info("Validating JSON");
        try {
            new JSONObject(text);
        } catch (JSONException ex) {
            try {
                new JSONArray(text);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares status code of given response with expected value.
     *
     * @param resp       HTTP response.
     * @param statusCode Expected status code.
     */
    public static void checkStatusCode(Response resp, int statusCode) {
        log.info("Checking status code");
        log.info("Response status code: " + resp.statusCode());
        log.info("Expected status code: " + statusCode);
        Assert.assertEquals(resp.statusCode(), statusCode);
    }

    /**
     * Compares two JSON with each other.
     *
     * @param json1 JSON string to be compared.
     * @param json2 JSON string to be compared.
     * @return True if JSONs are the same, else false.
     */
    public static boolean compareJson(String json1, String json2) {
        log.info("JSON compare");
        log.info("First JSON:\n" + json1);
        log.info("Second JSON:\n" + json2);

        final JSONObject requestJson = DataUtil.parseJson(json1);
        final JSONObject responseJson = DataUtil.parseJson(json2);

        return requestJson.similar(responseJson);
    }

    /**
     * Checks value under JSON key with expected value.
     *
     * @param json  JSON to be checked.
     * @param key   Key of field to be checked.
     * @param value Expected value.
     */
    public static void checkJsonValue(JSONObject json, String key, String value) {
        log.info("Checking JSON value");
        log.info("Expected: {\"" + key + "\":" + value + "}");
        log.info("Actual: {\"" + key + "\":" + json.get(key) + "}");
        Assert.assertEquals(json.get(key), value);
    }

    /**
     * Checks value under JSON key with expected value.
     *
     * @param jsonStr JSON string to be checked.
     * @param key     Key of field to be checked.
     * @param value   Expected value.
     */
    public static void checkJsonValue(String jsonStr, String key, String value) {
        final JSONObject json = new JSONObject(jsonStr);
        checkJsonValue(json, key, value);
    }

    public static void checkXmlValue(String xmlStr, String tag, String value) {
        final boolean isCorrect = xmlStr.contains("<" + tag + ">" + value + "</" + tag + ">");

        Matcher matcher = Pattern.compile("<" + tag + ">([a-zA-Z0-9 /\\-?:().,‘+]+)</" + tag + ">")
                .matcher(xmlStr);
        String actualValue = null;
        if (matcher.find()) {
            actualValue = matcher.group(1);
        }

        log.info("Expected value for tag '" + tag + "': " + value);
        log.info("Actual value for tag '" + tag + "': " + actualValue);
        Assert.assertTrue(isCorrect, "Value for tag " + tag + " should be " + value);
    }
}