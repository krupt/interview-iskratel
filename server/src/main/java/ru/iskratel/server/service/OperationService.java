package ru.iskratel.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class OperationService {

    private static final Logger log = LoggerFactory.getLogger(OperationService.class);

    private final UpdatableInRuntimeClassLoader classLoader = new UpdatableInRuntimeClassLoader((URLClassLoader) ClassLoader.getSystemClassLoader());

    private volatile Map<String, Operation> availableOperations = findAvailableOperations();

    public Response processRequest(Request request) {
        SessionService.initSession(request);
        final Operation operation = availableOperations.get(request.getOperationName());
        if (operation != null) {
            try {
                return operation.process(request);
            } catch (Exception e) {
                log.error("Processing request failed", e);
                return new Response("Exception when executing operation: " + e.getMessage(), null);
            }
        }
        return new Response("Unknown operation", null);
    }

    public void addJarToClassPath(URL url) {
        classLoader.addURL(url);
        availableOperations = findAvailableOperations();
    }

    private Map<String, Operation> findAvailableOperations() {
        final Map<String, Operation> operations = new HashMap<>();
        final ServiceLoader<Operation> loader = ServiceLoader.load(Operation.class, classLoader);
        loader.iterator()
                .forEachRemaining(operation -> operations.put(operation.getName(), operation));
        log.debug("Operations: {}", operations);
        return operations;
    }
}
