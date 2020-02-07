package com.sensiblemetrics.api.streamer.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Helper class to handle file read / write operations
 *
 * @author alexander.rogalskiy
 * @version 1.0
 */
@Slf4j
@UtilityClass
public class FileUtils {
    /**
     * Default file character encoding
     */
    public static final Charset DEFAULT_FILE_CHARACTER_ENCODING = StandardCharsets.UTF_8;

    public static List<String> readAllLines(final File inputFile) {
        Objects.requireNonNull(inputFile);
        try {
            return Files.readAllLines(inputFile.toPath(), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING);
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return Collections.emptyList();
    }

    public static List<String> readFileByFilter(final File inputFile, final Predicate<String> predicate) {
        Objects.requireNonNull(inputFile);
        List<String> resultList = Collections.emptyList();
        try (final BufferedReader br = Files.newBufferedReader(inputFile.toPath(), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING)) {
            resultList = br.lines().filter(predicate).collect(Collectors.toList());
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return resultList;
    }

    public static <U extends CharSequence> void writeFile(final File outputFile, final Map<Integer, List<String>> output) {
        Objects.requireNonNull(outputFile);
        Objects.requireNonNull(output);
        try (final PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile.toPath(), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING))) {
            output.forEach((key, value) -> {
                writer.println("-----------" + key + "-----------");
                writer.println(StringUtils.join(value, System.getProperty("line.separator")));
            });
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            log.error(String.format("ERROR: cannot create output file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        } catch (IOException ex) {
            log.error(String.format("ERROR: cannot process read / writer operations on file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        }
    }
}
