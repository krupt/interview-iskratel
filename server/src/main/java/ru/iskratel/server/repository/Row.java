package ru.iskratel.server.repository;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;

@ThreadSafe
@Immutable
public class Row<T> {

    private final T content;

    private final long lastUpdatedCommitId;

    public Row(T content, long lastUpdatedCommitId) {
        this.content = content;
        this.lastUpdatedCommitId = lastUpdatedCommitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Row row = (Row) o;
        return Objects.equals(content, row.content) &&
                lastUpdatedCommitId == row.lastUpdatedCommitId;
    }

    public T getContent() {
        return content;
    }

    public long getLastUpdatedCommitId() {
        return lastUpdatedCommitId;
    }
}
