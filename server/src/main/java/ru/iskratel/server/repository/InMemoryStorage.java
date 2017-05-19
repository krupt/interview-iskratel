package ru.iskratel.server.repository;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Changing rows internal state guarded by {@link java.util.concurrent.CopyOnWriteArrayList#lock}
 */
@ThreadSafe
public class InMemoryStorage<T, U> {

    private final List<Row<T, U>> rows = new CopyOnWriteArrayList<>();

    public void add(T content, U sign) {
        Row<T, U> row = new Row<>(content, lastUpdatedSign);
        row.setContent(content);
        row.setSign(sign);
        rows.add(row);
    }

    public Row<T, U> get(int index) {
        return rows.get(index);
    }

    public void set(int index, T content, U sign) {
        Row<T, U> row = new Row<>(content, lastUpdatedSign);
        row.setContent(content);
        row.setSign(sign);
        rows.set(index, row);
    }

    public void init(Collection<T> contents) {
        List<Row<T, U>> rows = contents.stream().map(t -> {
            Row<T, U> row = new Row<>(content, lastUpdatedSign);
            row.setContent(t);
            return row;
        }).collect(Collectors.toList());
        this.rows.addAll(rows);
    }
}
