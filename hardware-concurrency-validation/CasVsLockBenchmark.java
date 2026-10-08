import java.util.concurrent.atomic.AtomicInteger;

public class CasVsLockBenchmark {

    static int threads = 10;
    static int increments = 100000;

    static int lockCounter = 0;
    static AtomicInteger casCounter = new AtomicInteger(0);

    static synchronized void incrementWithLock() {
        lockCounter++;
    }

    static void incrementWithCAS() {
        while (true) {
            int oldValue = casCounter.get();
            int newValue = oldValue + 1;

            if (casCounter.compareAndSet(oldValue, newValue)) {
                break;
            }
        }
    }

    static long testLock() throws InterruptedException {

        lockCounter = 0;
        Thread[] workers = new Thread[threads];

        long start = System.nanoTime();

        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    incrementWithLock();
                }
            });

            workers[i].start();
        }

        for (Thread worker : workers) {
            worker.join();
        }

        return System.nanoTime() - start;
    }

    static long testCAS() throws InterruptedException {

        casCounter.set(0);
        Thread[] workers = new Thread[threads];

        long start = System.nanoTime();

        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    incrementWithCAS();
                }
            });

            workers[i].start();
        }

        for (Thread worker : workers) {
            worker.join();
        }

        return System.nanoTime() - start;
    }

    public static void main(String[] args) throws InterruptedException {

        long lockTime = testLock();
        long casTime = testCAS();

        int expected = threads * increments;

        System.out.println("CAS vs Lock");

        System.out.println();
        System.out.println("Lock:");
        System.out.println("Expected = " + expected);
        System.out.println("Actual   = " + lockCounter);
        System.out.println("Time     = " + lockTime / 1_000_000.0 + " ms");

        System.out.println();
        System.out.println("CAS:");
        System.out.println("Expected = " + expected);
        System.out.println("Actual   = " + casCounter.get());
        System.out.println("Time     = " + casTime / 1_000_000.0 + " ms");
    }
}