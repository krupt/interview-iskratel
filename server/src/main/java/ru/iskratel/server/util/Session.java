package ru.iskratel.server.util;

import java.util.UUID;

public class Session {

    private final UUID id = UUID.randomUUID();

    private volatile long lastCommitId;

    public UUID getId() {
        return id;
    }

    public long getLastCommitId() {
        return lastCommitId;
    }

    public void setLastCommitId(long lastCommitId) {
        this.lastCommitId = lastCommitId;
    }
}
