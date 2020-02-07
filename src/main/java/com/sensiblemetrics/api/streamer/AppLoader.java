package com.sensiblemetrics.api.streamer;

import com.sensiblemetrics.api.streamer.processor.TextProcessor;

/**
 * Streamer Application class
 *
 * @author Alex
 * @version 1.0.0
 */
public class AppLoader {
    public static void main(final String[] args) {
        new TextProcessor().init(args);
    }
}
