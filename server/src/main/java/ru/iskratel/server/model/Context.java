package ru.iskratel.server.model;

import ru.iskratel.server.util.Session;

public class Context {

    private final Request request;

    private final Session session;

    public Context(Request request, Session session) {
        this.request = request;
        this.session = session;
    }

    public Request getRequest() {
        return request;
    }

    public Session getSession() {
        return session;
    }
}
