package ru.iskratel.server.service;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.util.Cache;
import ru.iskratel.server.util.ConcurrentExpirationCache;
import ru.iskratel.server.util.Session;

import java.util.concurrent.TimeUnit;

public class SessionService {

    private static final ThreadLocal<Session> sessionHolder = new ThreadLocal<>();
    private static volatile Cache<String, Session> sessionCache;

    public static void init(long expireTime, TimeUnit unit) {
        sessionCache = new ConcurrentExpirationCache<>(expireTime, unit);
    }

    public static Session getSession() {
        return sessionHolder.get();
    }

    public static void initSession(Request request) {
        Session session = sessionCache.get(request.getUsername());
        if (session == null) {
            session = new Session();
            sessionCache.put(request.getUsername(), session);
        }
        sessionHolder.set(session);
    }
}
