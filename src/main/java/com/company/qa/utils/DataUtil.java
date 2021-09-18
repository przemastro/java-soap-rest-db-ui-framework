package com.company.qa.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DataUtil {

    private DataUtil() {
    }

    /**
     * Parses YAML text.
     *
     * @param text Text to be parsed.
     * @return Map constructed from parsed YAML text.
     */
    public static Map<String, Object> parseYaml(String text) {
        Yaml yaml = new Yaml();
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        return yaml.load(inputStream);
    }

    /**
     * Parses JSON text.
     *
     * @param text Text to be parsed.
     * @return JSONObject constructed from parsed text.
     */
    public static JSONObject parseJson(String text) {
        return new JSONObject(text);
    }

    /**
     * Reads YAML from file.
     *
     * @param path Path to file.
     * @return Map constructed from parsed YAML text.
     */
    public static Map<String, Object> readYamlFromFile(String path) throws IOException {
        String text = readTextFromFile(path);
        return parseYaml(text);
    }

    /**
     * Reads JSON from file.
     *
     * @param path Path to file.
     * @return JSONObject constructed from parsed text.
     */
    public static JSONObject readJsonFromFile(String path) throws IOException {
        String text = readTextFromFile(path);
        return parseJson(text);
    }

    /**
     * Reads text from file.
     *
     * @param path Path to file.
     * @return Text.
     */
    public static String readTextFromFile(String path) throws IOException {
        log.info("Reading text from file: " + path);
        String text = "";
        text = Files.readString(Paths.get(path));
        return text;
    }

    /**
     * Reads query from file.
     *
     * @param fileName Name of query file.
     * @return Query text.
     */
    public static String readQueryFromFile(String fileName) throws IOException {
        return readTextFromFile(Configuration.SQL_SCRIPTS_PATH + fileName);
    }

    /**
     * Reads text from file in temp directory.
     *
     * @param path Path to file in temp directory.
     * @return Text.
     */
    public static String readTextFromTemporalFile(String path) throws IOException {
        return readTextFromFile("temp/" + path);
    }

    /**
     * Writes text to file.
     *
     * @param path Path to file.
     * @param text Text to be written.
     */
    public static void writeTextToFile(String path, String text) throws IOException {
        log.info("Writing text to file: " + path);
        if (!Files.exists(Paths.get(path))) {
            final File file = new File(path);
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }
        Files.writeString(Paths.get(path), text);
    }

    /**
     * Writes text to file in temp directory.
     *
     * @param path Path to file.
     * @param text Text to be written.
     */
    public static void writeTextToTemporalFile(String path, String text) throws IOException {
        writeTextToFile("temp/" + path, text);
    }

    /**
     * Clears temp directory.
     */
    public static void clearTemporalDirectory() {
        log.info("Clearing temp directory");
        try {
            FileUtils.cleanDirectory(new File("temp"));
        } catch (IOException e) {
            log.error("Failed to clear temp directory", e);
        }
    }

    /**
     * Copies directory content to given path.
     *
     * @param source      Directory to be copied.
     * @param destination Destination path.
     */
    public static void copyTo(String source, String destination) {
        File sourceDir = new File(source);
        if (sourceDir.isDirectory() && sourceDir.list().length == 0) {
            return;
        }
        log.info("Copying from " + source + " to " + destination);
        try {
            FileUtils.copyDirectory(new File(source), new File(destination));
        } catch (IOException e) {
            log.error("Failed to copy", e);
        }
    }

    /**
     * Parses text for file escape sequences.
     *
     * @param input Text to be parsed.
     * @return Text with loaded file content in place of parsed escape sequences.
     */
    public static String parseFileEscapes(String input) throws IOException {
        final String ESCAPE_PATTERN = "%FILE=([a-zA-Z0-9.\\-/_\\\\]+)%";
        Matcher matcher = Pattern.compile(ESCAPE_PATTERN)
                .matcher(input);
        while (matcher.find()) {
            String filename = matcher.group(1);
            String fileData = readTextFromTemporalFile(filename);
            input = input.replaceFirst(ESCAPE_PATTERN, fileData);
        }
        return input;
    }

}