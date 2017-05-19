package ru.iskratel.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iskratel.server.model.Context;
import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.Cache;
import ru.iskratel.server.util.Session;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public class OperationService {

    private static final Logger log = LoggerFactory.getLogger(OperationService.class);

    private final Cache<String, Session> sessionCache;
    private final UpdatableInRuntimeClassLoader classLoader = new UpdatableInRuntimeClassLoader((URLClassLoader) ClassLoader.getSystemClassLoader());

    private volatile Iterable<Operation> availableOperations;

    public OperationService(Cache<String, Session> sessionCache) {
        availableOperations = findAvailableOperations();
        this.sessionCache = sessionCache;
    }

    public Response processRequest(Request request) {
        Session session = sessionCache.get(request.getUsername());
        if (session == null) {
            session = new Session(request.getUsername());
            sessionCache.put(request.getUsername(), session);
        }
        Context context = new Context(request, session);
        for (Operation availableOperation : availableOperations) {
            if (availableOperation.isSupported(context)) {
                return availableOperation.process(context);
            }
        }
        return new Response("Unknown operation", null);
    }

    public void addJarToClassPath(URL url) {
        classLoader.addURL(url);
        availableOperations = findAvailableOperations();
    }

    private Iterable<Operation> findAvailableOperations() {
        Collection<Operation> operations = new ArrayList<>();
        ServiceLoader<Operation> loader = ServiceLoader.load(Operation.class, classLoader);
        loader.iterator().forEachRemaining(operations::add);
        log.debug("Operations: {}", operations);
        return operations;
    }
}
