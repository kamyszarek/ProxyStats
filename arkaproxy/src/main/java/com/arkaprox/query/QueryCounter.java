package com.arkaprox.query;

public class QueryCounter {

    private static QueryCounter instance;

    private long createCount;
    private long deleteCount;
    private long dropCount;
    private long insertCount;
    private long selectCount;
    private long updateCount;

    private QueryCounter() {

    }

    public static synchronized QueryCounter getInstance() {
        if (instance == null) {
            instance = new QueryCounter();
        }
        return instance;
    }

    public void resetQueryCounts() {
        createCount = 0;
        deleteCount = 0;
        dropCount = 0;
        insertCount = 0;
        selectCount = 0;
        updateCount = 0;
    }

    public long getCreateCount() {
        return createCount;
    }

    public void setCreateCount(long createCount) {
        this.createCount = createCount;
    }

    public long getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(long deleteCount) {
        this.deleteCount = deleteCount;
    }

    public long getDropCount() {
        return dropCount;
    }

    public void setDropCount(long dropCount) {
        this.dropCount = dropCount;
    }

    public long getInsertCount() {
        return insertCount;
    }

    public void setInsertCount(long insertCount) {
        this.insertCount = insertCount;
    }

    public long getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(long selectCount) {
        this.selectCount = selectCount;
    }

    public long getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(long updateCount) {
        this.updateCount = updateCount;
    }
}