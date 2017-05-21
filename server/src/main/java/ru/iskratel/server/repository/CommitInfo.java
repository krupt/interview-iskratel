package ru.iskratel.server.repository;

class CommitInfo {

    private final int index;

    /**
     * Reordering of list or change one line
     */
    private final boolean reordering;

    CommitInfo(int index, boolean reordering) {
        this.index = index;
        this.reordering = reordering;
    }

    int getIndex() {
        return index;
    }

    boolean isReordering() {
        return reordering;
    }
}
