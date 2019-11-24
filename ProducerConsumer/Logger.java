package ProducerConsumer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

class Logger {

    private final BlockingQueue<LogMsg> bq = new LinkedBlockingQueue<LogMsg>();
    private static final int batchSize = 500;
    private ExecutorService es = Executors.newFixedThreadPool(1);

    void start() throws IOException {
        File file = File.createTempFile("foo", ".log");
        final FileWriter writer = new FileWriter(file);
        this.es.execute(() -> {
            try {
                int curIdx = 0;
                long preFT = System.currentTimeMillis();
                while (true) {
                    LogMsg log = bq.poll(5, TimeUnit.SECONDS);
                    if (log != null) {
                        writer.write(log.toString());
                        ++curIdx;
                    }
                    if (curIdx <= 0) {
                        continue;
                    }
                    if (log != null && log.level == LEVEL.ERROR ||
                            curIdx == batchSize ||
                            System.currentTimeMillis() - preFT > 5000) {
                        writer.flush();
                        curIdx = 0;
                        preFT = System.currentTimeMillis();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void info(String msg) throws InterruptedException {
        bq.put(new LogMsg(LEVEL.INFO, msg));
    }

    void error(String msg) throws InterruptedException {
        bq.put(new LogMsg(
                LEVEL.ERROR, msg));
    }
}

enum LEVEL {
    INFO, ERROR
}

class LogMsg {
    LEVEL level;
    String msg;

    LogMsg(LEVEL lvl, String msg) {
    }

    public String toString() {
        return "";
    }
}
