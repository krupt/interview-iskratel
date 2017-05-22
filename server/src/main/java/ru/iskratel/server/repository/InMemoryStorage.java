package ru.iskratel.server.repository;

import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.util.Session;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryStorage<T> {

    private static final long COMMIT_HISTORY_CLEANER_INTERVAL = 5;

    private final List<T> rows = new CopyOnWriteArrayList<>();
    private final AtomicLong commitId = new AtomicLong(1);
    private final SortedMap<Long, CommitInfo> commitHistory = new ConcurrentSkipListMap<>();

    public InMemoryStorage() {
        Executors.newScheduledThreadPool(1)
                .scheduleWithFixedDelay(() -> {
                    final Map<String, Session> cacheStamp = SessionService.getCacheStamp();
                    final Optional<Long> minActiveCommitId = cacheStamp.values()
                            .stream()
                            .map(Session::getLastCommitId)
                            .min(Long::compare);
                    if (minActiveCommitId.isPresent()) {
                        final Iterator<Map.Entry<Long, CommitInfo>> obsoleteIterator = commitHistory.headMap(minActiveCommitId.get())
                                .entrySet().iterator();
                        while (obsoleteIterator.hasNext()) {
                            obsoleteIterator.next();
                            obsoleteIterator.remove();
                        }
                    }
                }, COMMIT_HISTORY_CLEANER_INTERVAL, COMMIT_HISTORY_CLEANER_INTERVAL, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unchecked")
    public Collection<T> getAll() {
        SessionService.getSession().setLastCommitId(commitId.get());
        return Arrays.asList((T[]) rows.toArray());
    }

    public void set(int index, T content) {
        rows.set(index, content);
        addChangeHistory(index);
    }

    public void add(int index, T content) {
        rows.add(index, content);
        addReorderingHistory(index);
    }

    public void remove(int index) {
        rows.remove(index);
        addReorderingHistory(index);
    }

    public boolean isStructChanged(int index, long fromCommitId) {
        return isStructOrRowChanged(index, fromCommitId, false);
    }

    public boolean isRowChanged(int index, long fromCommitId) {
        return isStructOrRowChanged(index, fromCommitId, true);
    }

    private boolean isStructOrRowChanged(int index, long fromCommitId, boolean checkRow) {
        final SortedMap<Long, CommitInfo> commitsAfter = commitHistory.tailMap(fromCommitId + 1);
        final Optional<Integer> minIndex = commitsAfter.values()
                .stream()
                .filter(CommitInfo::isReordering)
                .map(CommitInfo::getIndex)
                .min(Integer::compare);
        if (minIndex.isPresent() && index >= minIndex.get()) {
            return true;
        }
        if (checkRow) {
            for (CommitInfo commitInfo : commitsAfter.values()) {
                if (commitInfo.getIndex() == index) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addChangeHistory(int index) {
        addHistory(index, false);
    }

    private void addReorderingHistory(int index) {
        addHistory(index, true);
    }

    private void addHistory(int index, boolean reordering) {
        final long newCommitId = commitId.incrementAndGet();
        commitHistory.put(newCommitId, new CommitInfo(index, reordering));
        SessionService.getSession().setLastCommitId(newCommitId);
    }

    public void init(Collection<T> contents) {
        rows.addAll(contents);
    }
}
