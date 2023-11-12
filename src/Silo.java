public class Silo {

    private String program;
    private int accumulator;
    private int backup;
    private Port[] ports;

    public Silo(String program, int portCount) {
        this.program = program;
        this.accumulator = 0;
        this.backup = 0;
        this.ports = new Port[portCount];
        for (int i = 0; i < portCount; i++) {
            ports[i] = new Port("Port " + i);
        }
    }

    public void executeInstruction(String instruction) {
        // Implementation of instruction execution
        // Parse the instruction and execute accordingly
        String[] parts = instruction.split(" ");
        String command = parts[0];
        switch (command) {
            case "NOOP":
                // No operation
                break;
            case "MOVE":
                // Move data between ports
                int srcPort = Integer.parseInt(parts[1]);
                int destPort = Integer.parseInt(parts[2]);
                int value = ports[srcPort].read();
                ports[destPort].write(value);
                break;
            // Add other cases for different instructions
        }
    }

    public int readPort(int portIndex) {
        if (portIndex >= 0 && portIndex < ports.length) {
            return ports[portIndex].read();
        }
        return -1; // Invalid port index
    }

    public void writePort(int portIndex, int value) {
        if (portIndex >= 0 && portIndex < ports.length) {
            ports[portIndex].write(value);
        }
    }

    // Getters and Setters
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(int accumulator) {
        this.accumulator = accumulator;
    }

    public int getBackup() {
        return backup;
    }

    public void setBackup(int backup) {
        this.backup = backup;
    }

    public Port[] getPorts() {
        return ports;
    }

}