package kiku.mutil.unit.RE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class Callable {
    public Callable() {

    }
}

class Callback {

    HashMap<Integer, HashSet<Callable>> map = new HashMap<Integer, HashSet<Callable>>();

    public void registercallback2(int signal_id, Callable callback) {
        if (!map.containsKey(signal_id)) {

            HashSet<Callable> set = new HashSet<Callable>();
            set.add(callback);
            map.put(signal_id, set);
        } else {
            ((HashSet) map.get(signal_id)).add(callback);
        }

    }

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void unregistercallback2(int signal_id, Callable callback) {
        if (!map.containsKey(signal_id))
            return;

        lock.writeLock().lock();
        try {
            map.get(signal_id).remove(callback);
        } finally {
            lock.writeLock().unlock();
        }

    }

    //http://www.obsidianscheduler.com/blog/java-concurrency-part-2-reentrant-locks/

}
