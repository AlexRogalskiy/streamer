package com.sensiblemetrics.api.streamer.processor;

import com.sensiblemetrics.api.streamer.analyzer.StreamAnalyzer;
import com.sensiblemetrics.api.streamer.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Text processor class to operate on input / output text stream
 *
 * @author alexander.rogalskiy
 * @version 1.0
 */
@Slf4j
public class TextProcessor {
    /**
     * Default word count regex pattern
     */
    private static final String TOKEN_DELIMITER_REGEX_PATTERN = "[\\p{Punct}\\p{Space}]";
    /**
     * Default supported character set
     */
    private static final String TOKEN_CHARACTER_SET_REGEX_PATTERN = "[\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}]+";

    public void init(final String[] args) {
        log.info("Initializing command line processor...");
        final CmdLineProcessor cmdProcessor = new CmdLineProcessor(args);

        log.info("Initializing stream analyzer...");
        final StreamAnalyzer streamAnalyzer = new StreamAnalyzer();
        final List<String> lines = FileUtils.readAllLines(cmdProcessor.getInputSource());
        final Map<Integer, List<String>> resultMap = streamAnalyzer.process(lines, this.buildWordCountPredicate(TOKEN_DELIMITER_REGEX_PATTERN));
        FileUtils.writeFile(cmdProcessor.getOutputSource(), resultMap);
    }

    private Function<String, Integer> buildWordCountPredicate(final String regex) {
        return value -> Optional.ofNullable(value)
                .filter(StringUtils::isNotBlank)
                .map(str -> str.split(regex))
                .map(this::summarize)
                .orElse(0);
    }

    private int summarize(final String[] tokens) {
        return (int) Arrays.stream(tokens).filter(value -> value.matches(TOKEN_CHARACTER_SET_REGEX_PATTERN)).count();
    }
}