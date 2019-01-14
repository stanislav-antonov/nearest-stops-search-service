package pse;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

import java.util.ArrayList;
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

        try {
            logger = Log.create(config.getLogFile());
            mServer = HttpServer.create();
            mServer.bind(new InetSocketAddress(config.getPort()), -1);
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        // Test curl command:
        // curl --header "Content-Type: application/json" --request POST  --data '{ "point": {"lat": 34.0943, "lng": 33.4434}}' http://localhost:3333/nearestStops
        mServer.createContext(NearestStopsHttpHandler.uriNearestStops, new NearestStopsHttpHandler(mKdTree, logger));

        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop(1, "Tarifnaya", 34.4324234, 35.5534534));
        stops.add(new Stop(2, "Titova", 34.5453535, 34.7567567));
        stops.add(new Stop(3, "Opolchenskaya", 35.7657567, 35.1313131));
        stops.add(new Stop(4, "Dubovka", 36.1343243, 34.746736));
        stops.add(new Stop(5, "Pionerskaya", 35.994343, 34.6345453));
        stops.add(new Stop(6, "Komsomolskaya", 34.8899434, 34.2434324));
        mKdTree.buildRecursive(stops);

        mServer.start();
        logger.info("Server is running and listening on " + config.getPort());
    }
}
