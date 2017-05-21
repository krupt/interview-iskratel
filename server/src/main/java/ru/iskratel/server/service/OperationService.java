package ru.iskratel.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public class OperationService {

    private static final Logger log = LoggerFactory.getLogger(OperationService.class);

    private final UpdatableInRuntimeClassLoader classLoader = new UpdatableInRuntimeClassLoader((URLClassLoader) ClassLoader.getSystemClassLoader());

    private volatile Iterable<Operation> availableOperations = findAvailableOperations();

    public Response processRequest(Request request) {
        SessionService.initSession(request);
        for (Operation availableOperation : availableOperations) {
            if (availableOperation.isSupported(request)) {
                try {
                    return availableOperation.process(request);
                } catch (Exception e) {
                    log.error("Processing request failed", e);
                    return new Response("Exception when executing operation: " + e.getMessage(), null);
                }
            }
        }
        return new Response("Unknown operation", null);
    }

    public void addJarToClassPath(URL url) {
        classLoader.addURL(url);
        availableOperations = findAvailableOperations();
    }

    private Iterable<Operation> findAvailableOperations() {
        final Collection<Operation> operations = new ArrayList<>();
        final ServiceLoader<Operation> loader = ServiceLoader.load(Operation.class, classLoader);
        loader.iterator().forEachRemaining(operations::add);
        log.debug("Operations: {}", operations);
        return operations;
    }
}
