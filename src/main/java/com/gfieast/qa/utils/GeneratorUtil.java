package com.gfieast.qa.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class GeneratorUtil {

    private GeneratorUtil() {
    }

    public static List<String> generateSymbolSequence(String input) {
        final List<String> list = new ArrayList<>();
        for (String range : input.split("\\|")) {
            if (range.length() >= 3 && range.contains("-")) {
                for (char c = range.charAt(0); c <= range.charAt(2); c++) {
                    list.add(String.valueOf(c));
                }
            } else {
                list.add(range);
            }
        }
        log.info("Generated sequence: " + list);
        return list;
    }

    public static String generateTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd-HH-mm").format(Calendar.getInstance().getTime());
    }

}