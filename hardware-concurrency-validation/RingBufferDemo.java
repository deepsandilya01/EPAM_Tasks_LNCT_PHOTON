public class RingBufferDemo {

    static class RingBuffer {

        private int[] buffer;
        private int size;
        private int write = 0;
        private int read = 0;

        RingBuffer(int size) {
            this.size = size;
            buffer = new int[size];
        }

        synchronized void put(int value) throws InterruptedException {

            while (write - read == size) {
                wait();
            }

            buffer[write % size] = value;
            write++;

            notifyAll();
        }

        synchronized int get() throws InterruptedException {

            while (write == read) {
                wait();
            }

            int value = buffer[read % size];
            read++;

            notifyAll();

            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int total = 100000;
        RingBuffer buffer = new RingBuffer(8);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < total; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                long sum = 0;

                for (int i = 0; i < total; i++) {
                    sum += buffer.get();
                }

                System.out.println("Consumer finished");
                System.out.println("Sum = " + sum);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        long start = System.nanoTime();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        long end = System.nanoTime();

        double time = (end - start) / 1_000_000.0;
        double throughput = total / (time / 1000);

        System.out.println();
        System.out.println("Ring Buffer Test");
        System.out.println("Messages = " + total);
        System.out.println("Time = " + time + " ms");
        System.out.println("Throughput = " + throughput + " messages/sec");
    }
}