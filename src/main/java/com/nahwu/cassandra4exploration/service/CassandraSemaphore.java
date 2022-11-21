package com.nahwu.cassandra4exploration.service;

import java.util.concurrent.Semaphore;

public class CassandraSemaphore {

    private Semaphore semaphore;

    public CassandraSemaphore(int slotLimit) {
        semaphore = new Semaphore(slotLimit);
    }

    public boolean tryGettingPermissionToInsertIntoCassandra() {
        return semaphore.tryAcquire();
    }

    public void releasePermissionToInsertIntoCassandra() {
        semaphore.release();
    }

    public int availableSlots() {
        return semaphore.availablePermits();
    }
}
