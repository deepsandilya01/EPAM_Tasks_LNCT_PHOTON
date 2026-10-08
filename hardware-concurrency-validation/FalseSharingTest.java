public class FalseSharingTest {

    static final int ROUNDS = 100_000_000;

    static class NormalCounters {
        volatile long first = 0;
        volatile long second = 0;
    }

    static class PaddedCounters {
        volatile long first = 0;

        long p1, p2, p3, p4, p5, p6, p7;

        volatile long second = 0;
    }

    static long testNormal() throws InterruptedException {

        NormalCounters counters = new NormalCounters();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < ROUNDS; i++) {
                counters.first++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < ROUNDS; i++) {
                counters.second++;
            }
        });

        long start = System.nanoTime();

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        return System.nanoTime() - start;
    }

    static long testPadded() throws InterruptedException {

        PaddedCounters counters = new PaddedCounters();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < ROUNDS; i++) {
                counters.first++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < ROUNDS; i++) {
                counters.second++;
            }
        });

        long start = System.nanoTime();

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        return System.nanoTime() - start;
    }

    public static void main(String[] args) throws InterruptedException {

        long normalTime = testNormal();
        long paddedTime = testPadded();

        System.out.println("False Sharing Test");

        System.out.println();
        System.out.println("Without Padding:");
        System.out.println(normalTime / 1_000_000.0 + " ms");

        System.out.println();
        System.out.println("With Padding:");
        System.out.println(paddedTime / 1_000_000.0 + " ms");
    }
}