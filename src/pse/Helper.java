package pse;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Helper {

    public static final int rcOk = 200;
    public static final int rcBadRequest = 400;
    public static final int rcMethodNotAllowed = 405;

    public static void sendResponseBody(HttpExchange httpExchange, String body) throws IOException {
        if (body == null) {
            return;
        }

        OutputStream responseBody = null;

        try {
            responseBody = httpExchange.getResponseBody();
            responseBody.write(body.getBytes());
        } finally {
            if (responseBody != null) {
                try {
                    responseBody.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
