package TwoPhaseTermination;

class Proxy {

    private volatile boolean terminated = false;

    private boolean started = false;

    private Thread rptThread;

    synchronized void start() {
        if (started) {
            return;
        }
        started = true;
        terminated = false;
        rptThread = new Thread(() -> {
            while (!terminated) {
                report();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            started = false;
        });
        rptThread.start();
    }

    private void report() {

    }

    synchronized void stop() {
        terminated = true;
        rptThread.interrupt();
    }
}
