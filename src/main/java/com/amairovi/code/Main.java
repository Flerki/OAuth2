package com.amairovi.code;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    final static String CLIENT_ID = "clientId";
    static String secretCode = null;

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
//        http://localhost:8000/authorize?client_id=clientId&response_type=code&redirect_uri=https%3A%2F%2Fclient.com
        server.createContext("/authorize", new AuthorizationHandler());
        server.createContext("/authenticate", new AuthenticationHandler());

// a24c9863-9eaa-4b54-a3ff-d4c499dcb266
        //        http://localhost:8000/token?grant_type=authorization_code&redirect_uri=https%3A%2F%2Fclient.com&code=
        server.createContext("/token", new TokenHandler());
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
