package com.amairovi.code;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static com.amairovi.code.Main.parseQuery;
import static com.amairovi.code.Main.secretCode;

public class TokenHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        Map<String, String> queryParamNameToValue = parseQuery(t.getRequestURI().getQuery());

        String code = queryParamNameToValue.get("code");

        if (code.equals(secretCode)){
            String response = "response with access token";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "error=access_denied";
            t.sendResponseHeaders(400, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }

    }

}
