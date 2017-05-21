package ru.iskratel.server.util;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.repository.Row;

import java.util.UUID;

public class Session {

    private final UUID id = UUID.randomUUID();

    private volatile int lastRowId;

    private volatile long lastReadCommitId;

    private volatile Request lastRequest;

    private volatile boolean confirm;

    public UUID getId() {
        return id;
    }

    public int getLastRowId() {
        return lastRowId;
    }

    public void setLastRowId(int lastRowId) {
        this.lastRowId = lastRowId;
    }

    public long getLastReadCommitId() {
        return lastReadCommitId;
    }

    public void setLastReadCommitId(long lastReadCommitId) {
        this.lastReadCommitId = lastReadCommitId;
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
