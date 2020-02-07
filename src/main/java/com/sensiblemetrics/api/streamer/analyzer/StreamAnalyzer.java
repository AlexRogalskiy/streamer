package com.sensiblemetrics.api.streamer.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class to process streams
 *
 * @author alexander.rogalskiy
 * @version 1.0
 */
@Slf4j
public class StreamAnalyzer {

    /**
     * Returns {@link Map} collection of grouped {@link List} strings by input {@link List} collection of strings
     *
     * @param lines - initial input {@link List} collection of strings to operate by
     * @return {@link Map} collection of grouped {@link List} strings
     */
    public ConcurrentMap<Integer, List<String>> process(final List<String> lines, final Function<? super String, ? extends Integer> processor) {
        return Optional.ofNullable(lines)
                .filter(CollectionUtils::isNotEmpty)
                .<ConcurrentMap<Integer, List<String>>>map(data -> data.stream().parallel().collect(Collectors.groupingByConcurrent(processor)))
                .orElseGet(ConcurrentHashMap::new);
    }
}
