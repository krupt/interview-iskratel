package ru.iskratel.server.repository;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Changing rows internal state guarded by {@link java.util.concurrent.CopyOnWriteArrayList#lock}
 */
@ThreadSafe
public class InMemoryStorage<T> {

    private final List<Row<T>> rows = new CopyOnWriteArrayList<>();

    public void add(T lineContent) {

    }
}
