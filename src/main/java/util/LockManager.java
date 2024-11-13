package util;

import exception.ConcurrencyException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LockManager {
    private static final LockManager instance = new LockManager();

    // <listingId, sessionId>
    private final ConcurrentMap<Long, String> lockMap = new ConcurrentHashMap<>();

    public static synchronized LockManager getInstance() {
        return instance;
    }

    public void acquireLock(long lockable, String owner) throws ConcurrencyException {
        if (!lockMap.containsKey(lockable)) {
            lockMap.put(lockable, owner);
        } else {
            throw new ConcurrencyException();
        }
    }

    public void releaseLock(long lockable, String owner) {
        if (lockMap.containsKey(lockable) && lockMap.get(lockable).equals(owner)) lockMap.remove(lockable);
    }
}
