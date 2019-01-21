package pse;

import java.util.logging.Logger;

public class Updater {

    final private Config mConfig;
    final private Logger mLogger;
    final private KdTree mKdTree;

    public Updater(Config config, Logger logger, KdTree kdTree) {
        mConfig = config;
        mLogger = logger;
        mKdTree = kdTree;
    }

}
