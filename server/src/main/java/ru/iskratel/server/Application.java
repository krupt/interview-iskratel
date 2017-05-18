package ru.iskratel.server;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iskratel.server.exception.InvalidConfigurationException;
import ru.iskratel.server.util.Cache;
import ru.iskratel.server.util.ConcurrentExpirationCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    }

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        Properties applicationProperties = new Properties();
        try (InputStream propertiesStream = Application.class.getResourceAsStream("/application.properties")) {
            applicationProperties.load(propertiesStream);
        } catch (IOException e) {
            throw new InvalidConfigurationException("Couldn't read properties file", e);
        }
        int port;
        try {
            port = Integer.parseInt(applicationProperties.getProperty("port"));
        } catch (Exception e) {
            throw new InvalidConfigurationException("Invalid parameter 'port'", e);
        }
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Cache<String, String> sessionCache = new ConcurrentExpirationCache<>(10, TimeUnit.SECONDS);
        /*while (true) {
            System.out.println("Waiting connections...");
            final Socket socket = serverSocket.accept();
            executorService.execute(() -> {
                try (InputStream input = socket.getInputStream();
                     PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                    ObjectReader reader = mapper.readerFor(Request.class);
                    Request request = reader.readValue(input);
                    System.out.println("request = " + request);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Response sent");
                    Response response = new Response("hello", "nadsdaspiadads");
                    mapper.writeValue(output, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }*/
        sessionCache.put("kovalev", "12345");
        log.debug("Putted \"kovalev\"");
        sessionCache.put("frolov", "123");
        log.debug("Putted \"frolov\"");
        log.debug("Started");
        new Thread(() -> {
            try {
                Thread.sleep(4000);
                log.debug("Getted \"kovalev\": {}", sessionCache.get("kovalev"));
                sessionCache.put("plekhanov", "543");
                log.debug("Putted \"plekhanov\"");
                Thread.sleep(8975);
                log.debug("Getted \"frolov\": {}", sessionCache.get("frolov"));
                log.debug("Getted \"kovalev\": {}", sessionCache.get("kovalev"));
                sessionCache.put("kovalev", "654321");
                log.debug("Putted new value for \"kovalev\"");
                Thread.sleep(10000);
                log.debug("Getted \"kovalev\": {}", sessionCache.get("kovalev"));
            } catch (InterruptedException e) {
                // Ignore
            }
        }).start();
    }
}
