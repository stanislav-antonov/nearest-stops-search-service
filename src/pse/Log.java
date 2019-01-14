package pse;

import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Log {
    public static java.util.logging.Logger create(String logFile) throws Exception {
        final FileHandler fileHandler = new FileHandler(logFile);
        fileHandler.setFormatter(new SimpleFormatter());

        final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("main");
        logger.addHandler(fileHandler);

        return logger;
    }
}
