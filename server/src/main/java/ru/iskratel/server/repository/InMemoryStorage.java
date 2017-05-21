package ru.iskratel.server.repository;

import ru.iskratel.server.service.SessionService;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Changing rows internal state guarded by {@link java.util.concurrent.CopyOnWriteArrayList#lock}
 */
@ThreadSafe
public class InMemoryStorage<T> {

    private final List<Row<T>> rows = new CopyOnWriteArrayList<>();
    private final AtomicLong commitId = new AtomicLong(1);
    private final SortedMap<Long, Integer> commitHistory = new ConcurrentSkipListMap<>();

    public Row<T> get(int index) {
        Row<T> row = rows.get(index);
        SessionService.getSession().setLastReadCommitId(commitId.get());
        return row;
    }

    public void set(int index, T content, long previousCommitId) {
        Row<T> row = new Row<>(content, commitId.incrementAndGet());
        rows.set(index, row);
    }

    public void add(T content, int index, long previousCommitId) {
        long newCommitId = this.commitId.incrementAndGet();
        Row<T> row = new Row<>(content, newCommitId);
        rows.add(index, row);
        commitHistory.put(newCommitId, index);
    }

    public void remove(int index, long previousCommitId) {
        long newCommitId = this.commitId.incrementAndGet();
        rows.remove(index);
        commitHistory.put(newCommitId, index);
    }

    public boolean isRowChanged(int index, long fromCommitId, long rowCommitId) {
        // TODO
        return false;
    }

    public void init(Collection<T> contents) {
        List<Row<T>> rows = contents.stream()
                .map(line -> new Row<>(line, commitId.get()))
                .collect(Collectors.toList());
        this.rows.addAll(rows);
    }
}
