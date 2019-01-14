package pse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

final class NearestStopsHttpHandler implements HttpHandler {

    public static final String uriNearestStops = "/nearestStops";

    private final Logger mLogger;
    private final KdTree mKdTree;

    private static class RequestBody {

        private static class Point {
            @Expose
            @SerializedName("lat")
            private double mLat;

            @Expose
            @SerializedName("lng")
            private double mLng;
        }

        @Expose
        @SerializedName("point")
        private Point mPoint;

        public Point getPoint() {
            return mPoint;
        }
    }

    NearestStopsHttpHandler(KdTree kdTree, Logger logger) {
        mLogger = logger;
        mKdTree = kdTree;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        mLogger.log(Level.INFO, "Start serving request: " + httpExchange.getRequestURI());

        final String requestMethod = httpExchange.getRequestMethod();

        if (!requestMethod.equals("POST")) {
            httpExchange.sendResponseHeaders(Helper.rcMethodNotAllowed, 0);
            Helper.sendResponseBody(httpExchange, "Method not allowed");
            mLogger.log(Level.WARNING, "Method not allowed: " + requestMethod);

            return;
        }

        Double[] queryPoint;

        try {
            queryPoint = obtainQueryPoint(httpExchange);
            mLogger.log(Level.INFO, "Query point {0}, {1}", queryPoint);
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(Helper.rcBadRequest, 0);
            Helper.sendResponseBody(httpExchange, "Bad request");
            mLogger.log(Level.WARNING, "Bad request: " + e.toString());

            return;
        }

        final List<Stop> nearestStops = obtainNearestStops(queryPoint);
        final String result = new Gson().toJson(nearestStops);

        httpExchange.sendResponseHeaders(Helper.rcOk, 0);
        Helper.sendResponseBody(httpExchange, result);
    }

    private List<Stop> obtainNearestStops(Double[] queryPoint) {
        final List<Stop> result = new ArrayList<>();
        final List<Node> nearestNodes = mKdTree.kNearestNeighbour(queryPoint, 3);

        for (Node node : nearestNodes) {
            Stop stop = (Stop) node.getData();
            result.add(stop);
        }

        return result;
    }

    private static Double[] obtainQueryPoint(HttpExchange httpExchange) throws JsonIOException, JsonSyntaxException {
        InputStreamReader reader = null;

        try {
            reader = new InputStreamReader(httpExchange.getRequestBody());
            final RequestBody requestBody = new Gson().fromJson(reader, RequestBody.class);

            return new Double[] { requestBody.mPoint.mLat, requestBody.mPoint.mLng };
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
