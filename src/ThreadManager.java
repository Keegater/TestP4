public class ThreadManager {


    //create and store threads

    //have threads return shit after?

    //has an array of booleans. Each index referes to a different silo. If the whole array = true, next step is ready.

    //silos are stored row by row. Ex for 2x2: {(r0,c0), (r0,c1), (r1, c0), (r1, c1)}
    Silo[] silos;
    InOutPut[] inOutPuts;
    Port[] ports;

    public boolean threadsReady;



    public ThreadManager(Silo[] silos, InOutPut[] inOutPuts, Port[] ports ){

        this.silos = silos;
        this.inOutPuts = inOutPuts;
        this.ports = ports;
        this.threadsReady = true;

    }


    public void startThreads(){
        for (Silo silo : silos){
            silo.start();
        }
        for (Port port : ports){
            port.start();
        }
        for (InOutPut put : inOutPuts){
            put.start();
        }
        waitForThreads();
    }

    private void waitForThreads() {
        for (Silo silo : silos ) {
            try {
                silo.join(); // Wait for this thread to die
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                throw new RuntimeException("Thread waiting was interrupted", e);
            }
        }
        threadsReady = true;
        System.out.println("AllDone");
    }

    public boolean threadsReady(){
        int notready = 0;
        for (Silo silo : silos){
            if (!silo.ready){
                if (!silo.idle){
                    notready++;
                }
            }
        }
        return notready == 0;
    }

    //testing
    public void printThreadStatus(){
        System.out.println(silos[0].getState().toString());
    }





}