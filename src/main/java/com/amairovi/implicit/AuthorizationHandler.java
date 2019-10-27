package com.amairovi.implicit;

import com.amairovi.code.Main;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Map;

import static com.amairovi.implicit.Main.CLIENT_ID;
import static com.amairovi.implicit.Main.parseQuery;

public class AuthorizationHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        Map<String, String> queryParamNameToValue = parseQuery(t.getRequestURI().getQuery());

        String redirectUri = queryParamNameToValue.get("redirect_uri");
        String responseType = queryParamNameToValue.get("response_type");

        if (!responseType.equals("token")) {
            Headers headers = t.getResponseHeaders();
            headers.set("Location", redirectUri + "?error=invalid_request");
            t.sendResponseHeaders(302, 0);
        }

        String clientId = queryParamNameToValue.get("client_id");
        if (!clientId.equals(CLIENT_ID)) {
            Headers headers = t.getResponseHeaders();
            headers.set("Location", redirectUri + "?error=unauthorized_client");
            t.sendResponseHeaders(302, 0);
        }


        String page = readAuthPage().replace("{{redirect_uri}}", redirectUri);
        t.sendResponseHeaders(200, page.length());
        OutputStream os = t.getResponseBody();
        os.write(page.getBytes());
        os.close();
    }

    private String readAuthPage() {
        InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream("code/authenticate.html");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line;
        StringBuilder res = new StringBuilder();
        try {
            line = reader.readLine();
            while (line != null) {
                res.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res.toString();
    }

}
