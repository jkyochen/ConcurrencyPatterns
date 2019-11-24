package ProducerConsumer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Batch {

    private BlockingQueue<Task> bq = new LinkedBlockingQueue<Task>(2000);

    void start() {

        ExecutorService es = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                try {
                    while (true) {
                        List<Task> ts = pollTasks();
                        execTasks(ts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private List<Task> pollTasks() throws InterruptedException {
        List<Task> ts = new LinkedList<Task>();
        Task t = bq.take();
        while (t != null) {
            ts.add(t);
            t = bq.poll();
        }
        return ts;
    }

    private void execTasks(List<Task> ts) {

    }
}

class Task {
}
