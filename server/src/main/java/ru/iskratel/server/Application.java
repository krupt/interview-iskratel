package ru.iskratel.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iskratel.server.exception.InvalidConfigurationException;
import ru.iskratel.api.model.Request;
import ru.iskratel.api.model.Response;
import ru.iskratel.server.repository.InMemoryStorage;
import ru.iskratel.server.service.OperationService;
import ru.iskratel.server.service.SessionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static volatile Application instance;

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final OperationService operationService;
    private final InMemoryStorage<String> storage;

    static {
        mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    public static Application getInstance() {
        if (instance == null) {
            synchronized (Application.class) {
                if (instance == null) {
                    try {
                        instance = new Application();
                    } catch (InvalidConfigurationException e) {
                        log.error("", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        new Thread(getInstance()).start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //noinspection InfiniteLoopStatement
        while (true) {
            final String line = reader.readLine();
            try {
                final Path path = Paths.get(line);
                final URL url = path.toUri().toURL();
                log.info("Loading {} to classpath", url);
                getInstance().operationService.addJarToClassPath(url);
                log.info("{} loaded", url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Application() throws InvalidConfigurationException {
        final Properties applicationProperties = new Properties();
        try (InputStream propertiesStream = this.getClass().getResourceAsStream("/application.properties")) {
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
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new InvalidConfigurationException("Couldn't create server socket", e);
        }
        int sessionTimeoutMinutes;
        try {
            sessionTimeoutMinutes = Integer.parseInt(applicationProperties.getProperty("session.timeout"));
            SessionService.init(sessionTimeoutMinutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new InvalidConfigurationException("Invalid session timeout", e);
        }
        executorService = Executors.newFixedThreadPool(100);
        storage = new InMemoryStorage<>();
        Collection<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(this.getClass().getResource("/basic_lines.txt").toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new InvalidConfigurationException("Couldn't read basic lines from file", e);
        }
        storage.init(lines);
        operationService = new OperationService();
    }

    public void run() {
        while (!Thread.interrupted()) {
            log.debug("Waiting connections...");
            try {
                final Socket socket = serverSocket.accept();
                log.info("New connection from {}", socket);
                executorService.execute(() -> {
                    try (InputStream input = socket.getInputStream();
                         PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                        final ObjectReader reader = mapper.readerFor(Request.class);
                        final Request request = reader.readValue(input);
                        log.debug("request = {}", request);
                        final Response response = operationService.processRequest(request);
                        log.debug("response = {}", response);
                        mapper.writeValue(output, response);
                    } catch (IOException e) {
                        log.error("Exception when processing data", e);
                    }
                });
            } catch (IOException e) {
                log.error("Accepting connection failed", e);
            }
        }
    }

    public InMemoryStorage<String> getStorage() {
        return storage;
    }
}
