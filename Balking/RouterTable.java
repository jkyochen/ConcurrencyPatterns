package Balking;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;

final class Router {

    final String ip;
    final Integer port;
    final String iface;

    public Router(String ip, Integer port, String iface) {
        this.ip = ip;
        this.port = port;
        this.iface = iface;
    }
}

public class RouterTable {

    private ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> rt = new ConcurrentHashMap<>();

    private volatile boolean changed;

    private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public void startLocalSaver() {
        ses.scheduleWithFixedDelay(this::autoSave, 1, 1, MINUTES);
    }

    private void autoSave() {
        if (!changed) {
            return;
        }
        changed = false;
        this.save2Local();
    }

    private void save2Local() {

    }

    public void remove(Router router) {
        Set<Router> set = rt.get(router.iface);
        if (set != null) {
            set.remove(router);
            changed = true;
        }
    }

    public void add(Router router) {
        Set<Router> set = rt.computeIfAbsent(router.iface, r -> new CopyOnWriteArraySet<>());
        set.add(router);
        changed = true;
    }
}
