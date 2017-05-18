package ru.iskratel.server.repository;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Objects;

@NotThreadSafe
class Row<T> {

    private T content;

    private String lastUpdatedSessionId;

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
                Objects.equals(lastUpdatedSessionId, row.lastUpdatedSessionId);
    }

    /*@Override
    public int hashCode() {
        return Objects.hash(content, lastUpdatedSessionId);
    }*/
}
