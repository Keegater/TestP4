public class Port {

    private String name;
    private int value;
    private boolean isAvailable;

    public Port(String name) {
        this.name = name;
        this.value = 0;
        this.isAvailable = true;
    }

    public synchronized int read() {
        while (!isAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }
        isAvailable = false;
        notifyAll();
        return value;
    }

    public synchronized void write(int value) {
        while (isAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.value = value;
        isAvailable = true;
        notifyAll();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}