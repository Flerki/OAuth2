package com.amairovi.implicit;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.amairovi.implicit.Main.parseQuery;
import static com.amairovi.implicit.Main.token;

public class AuthenticationHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        Map<String, String> queryParamNameToValue = parseQuery(t.getRequestURI().getQuery());
        String redirectUri = queryParamNameToValue.get("redirect_uri");

        token = UUID.randomUUID().toString();

        Headers headers = t.getResponseHeaders();
        headers.set("Location", redirectUri + "#access_token=" + token);
        t.sendResponseHeaders(302, 0);
    }
}
