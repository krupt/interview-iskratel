package ru.iskratel.server.util;

import ru.iskratel.server.model.Request;

import java.util.UUID;

public class Session {

    private final UUID id = UUID.randomUUID();

    private volatile long lastCommitId;

    private volatile Request lastRequest;

    private volatile boolean confirm;

    public UUID getId() {
        return id;
    }

    public long getLastCommitId() {
        return lastCommitId;
    }

    public void setLastCommitId(long lastCommitId) {
        this.lastCommitId = lastCommitId;
    }

    public Request getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Request lastRequest) {
        this.lastRequest = lastRequest;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }
}
