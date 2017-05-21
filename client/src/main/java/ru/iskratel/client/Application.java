package ru.iskratel.client;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import ru.iskratel.api.model.Request;
import ru.iskratel.api.model.Response;
import ru.iskratel.client.exception.InvalidConfigurationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

public class Application {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static String username;
    private static String host;
    private static int port;

    static {
        mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    public static void main(String[] args) throws IOException {
        final Properties applicationProperties = new Properties();
        try (InputStream propertiesStream = Application.class.getResourceAsStream("/application.properties")) {
            applicationProperties.load(propertiesStream);
        } catch (IOException e) {
            throw new InvalidConfigurationException("Couldn't read properties file", e);
        }
        host = applicationProperties.getProperty("host");
        try {
            port = Integer.parseInt(applicationProperties.getProperty("port"));
        } catch (Exception e) {
            throw new InvalidConfigurationException("Invalid parameter 'port'", e);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter username: ");
            username = reader.readLine();
            if (username == null || username.trim().length() == 0) {
                System.out.println("Username can't be null");
                return;
            }
            String commandLine;
            while (true) {
                System.out.println("Enter command in format: operation[/index][/content]");
                commandLine = reader.readLine();
                if ("exit".equalsIgnoreCase(commandLine)) {
                    break;
                }
                final String[] arguments = commandLine.split("/");
                final String operation = arguments[0];
                Integer index = null;
                if (arguments.length > 1) {
                    try {
                        index = Integer.parseInt(arguments[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid parameter \"index\"");
                    }
                }
                String content = null;
                if (arguments.length > 2) {
                    content = arguments[2];
                }
                final Request request = new Request();
                request.setUsername(username);
                request.setOperationName(operation);
                request.setIndex(index);
                request.setContent(content);
                try {
                    Response response = sendRequest(request);
                    if (response.getMessage() != null) {
                        System.out.println(response.getMessage());
                    }
                    if (response.getContent() != null) {
                        System.out.println("Content:");
                        System.out.println(response.getContent().replaceAll("\n", System.lineSeparator()));
                    }
                } catch (Exception e) {
                    System.out.println("Transfer request to server failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private static Response sendRequest(Request request) throws IOException {
        final Socket socket = new Socket(host, port);
        try (OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream()) {
            mapper.writeValue(output, request);
            ObjectReader objectReader = mapper.readerFor(Response.class);
            return objectReader.readValue(input);
        }
    }
}
