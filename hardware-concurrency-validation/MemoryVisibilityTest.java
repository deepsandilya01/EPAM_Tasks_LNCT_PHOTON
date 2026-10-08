public class MemoryVisibilityTest {

    static int data = 0;
    static volatile boolean ready = false;

    public static void main(String[] args) throws InterruptedException {

        Thread writer = new Thread(() -> {
            data = 42;
            ready = true;

            System.out.println("Writer: data is ready");
        });

        Thread reader = new Thread(() -> {

            while (!ready) {
                // wait
            }

            System.out.println("Reader: data = " + data);
        });

        reader.start();
        writer.start();

        writer.join();
        reader.join();

        System.out.println("Done");
    }
}