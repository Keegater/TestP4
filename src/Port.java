public class Port extends Thread {

    private int value;
    private boolean hasValue;
    private boolean isAvailable;

    public Port() {
        this.value = 0;
        this.hasValue = false;
        this.isAvailable = true;
    }


    @Override
    public void run(){
        //System.out.println("Port running");

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

    //synchronized key word prevents any other silos from calling this methood while another is.
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



    public int getValue() {
        return value;
    }

    public boolean hasValue(){
        return hasValue;
    }

    public boolean isAvailable() {
        return isAvailable;
    }


}
