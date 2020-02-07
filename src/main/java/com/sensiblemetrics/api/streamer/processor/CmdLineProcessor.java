package com.sensiblemetrics.api.streamer.processor;

import com.sensiblemetrics.api.streamer.AppLoader;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

import java.io.File;

/**
 * Class to process input CLI arguments
 *
 * @author alexander.rogalskiy
 * @version 1.0
 */
@Slf4j
public final class CmdLineProcessor {

    @Option(name = "-in", aliases = {"--input"}, required = true, usage = "sets input text file", metaVar = "INPUT FILE")
    private File inputSource;
    @Option(name = "-out", aliases = {"--output"}, required = true, usage = "sets output text file", metaVar = "OUTPUT FILE")
    private File outputSource;
    @Option(name = "-i", aliases = {"--ignore-case"}, required = false, usage = "enables/disables ignore case mode", metaVar = "IGNORE CASE MODE", handler = ExplicitBooleanOptionHandler.class)
    private boolean ignoreCase;
    // Error flag
    private boolean errorFlag = false;

    public CmdLineProcessor(final String... args) {
        final CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);

            if (null != getInputSource() && !getInputSource().isFile()) {
                throw new CmdLineException(parser, "Invalid argument: --input is not a valid input file.", null);
            }
            if (null == getOutputSource() || !getOutputSource().isFile()) {
                throw new CmdLineException(parser, "Invalid argument: --output is not a valid output file.", null);
            }
            errorFlag = true;
        } catch (CmdLineException ex) {
            log.error(String.format("ERROR: cannot parse input / output arguments, message=(%s)", ex.getMessage()));
            log.error(String.format("Example: java %s %s", AppLoader.class.getName(), parser.printExample(OptionHandlerFilter.ALL)));
        }
    }

    /**
     * Returns whether the parameters could be parsed without an error.
     *
     * @return true if no error occurred, false - otherwise
     */
    public boolean isErrorFree() {
        return this.errorFlag;
    }

    /**
     * Returns the input source file.
     *
     * @return The input source file.
     */
    public File getInputSource() {
        return this.inputSource;
    }

    /**
     * Returns the output source file.
     *
     * @return The output source file.
     */
    public File getOutputSource() {
        return this.outputSource;
    }

    /**
     * Returns the flag of ignore case option
     *
     * @return boolean (true - if ignore case flag has been set, false -
     * otherwise)
     */
    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }
}
