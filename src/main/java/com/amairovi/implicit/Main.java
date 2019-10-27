package com.amairovi.implicit;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    final static String CLIENT_ID = "clientId";
    static String token = null;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
//        http://localhost:8000/authorize?client_id=clientId&response_type=token&redirect_uri=https%3A%2F%2Fclient.com
        server.createContext("/authorize", new AuthorizationHandler());
        server.createContext("/authenticate", new AuthenticationHandler());

        server.setExecutor(null);
        server.start();
    }

    static Map<String, String> parseQuery(String query) {
        String[] params = query.split("&");
        return Arrays.stream(params)
                .map(param -> param.split("="))
                .collect(Collectors.toMap(nameToValue -> nameToValue[0], nameToValue -> nameToValue[1]));
    }
}
