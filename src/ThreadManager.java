import java.util.ArrayList;
import java.util.List;

public class ThreadManager {

    private List<Thread> threads;

    public ThreadManager() {
        this.threads = new ArrayList<>();
    }

    public void addThread(Runnable task) {
        Thread thread = new Thread(task);
        threads.add(thread);
    }

    public void startAllThreads() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void joinAllThreads() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Getters
    public List<Thread> getThreads() {
        return threads;
    }

}