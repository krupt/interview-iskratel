package ru.iskratel.server.util;

import ru.iskratel.server.model.Operation;

import java.util.UUID;

public class Session {

    private final UUID id;

    private final String username;

    private volatile Operation lastOperation;

    public Session(String username) {
        this.username = username;
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Operation getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(Operation lastOperation) {
        this.lastOperation = lastOperation;
    }
}
