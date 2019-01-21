package pse;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static HttpServer mServer;
    private static final KdTree mKdTree = new KdTree();

    public static void main(String[] args) {

        final Config config = Config.create();

        try {
            config.load();
        } catch (Exception e) {
            System.out.println("Failed to load config defaults, could not run.");
            e.printStackTrace();
            return;
        }

        if (!config.overridesLoaded()) {
            System.out.println("No config overrides has been loaded, using defaults to run.");
        }

        final Logger logger;
        final StopProvider provider;

        try {
            logger = Log.create(config.getLogFile());
            provider = new StopProvider(config, logger);

            mServer = HttpServer.create();
            mServer.bind(new InetSocketAddress(config.getPort()), -1);
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        // Test curl command:
        // curl --header "Content-Type: application/json" --request POST  --data '{ "point": {"lat": 34.0943, "lng": 33.4434}}' http://localhost:3333/nearestStops
        mServer.createContext(NearestStopsHttpHandler.uriNearestStops, new NearestStopsHttpHandler(mKdTree, logger));

        try {
            final List<Stop> stops = provider.getList();
            mKdTree.buildIterative(stops);

            mServer.start();
            logger.info("Server is running and listening on " + config.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
