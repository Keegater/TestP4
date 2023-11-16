import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Silo extends Thread {

    private List<String> program;
    private List<Instruction> instructions = new ArrayList<>();
    private int accumulator;
    private int backup;
    private Port upPort, downPort, leftPort, rightPort;
    private int move;

    private Port[] ports;
    private ThreadManager threadManager;

    public boolean ready = false;
    public boolean idle = false;

    //GUI stuff




    //add @OVERRIDE run method.   This should try to run the current instruction,


    public Silo(List<String> program) {
        this.program = program;
        this.accumulator = 0;
        this.backup = 0;
        this.move = 0;
        upPort = null;
        downPort = null;
        leftPort = null;
        rightPort = null;
        parseProgram(program);
    }

    @Override
    public void run(){

        executeNext();
        move++;

    }


    public void setPort(String side, Port port){
        switch (side.toUpperCase(Locale.ROOT)) {
            case "UP" -> upPort = port;
            case "DOWN" -> downPort = port;
            case "LEFT" -> leftPort = port;
            case "RIGHT" -> rightPort = port;
        }
    }


    public int readPort(String port) {
        idle = true;
        switch (port){
            case ("UP"):
                if (upPort != null){
                    return upPort.read();
                }
                break;
            case ("DOWN"):
                if (downPort != null){
                    return downPort.read();
                }
                break;
            case ("LEFT"):
                if (leftPort != null){
                    return leftPort.read();
                }
                break;
            case ("RIGHT"):
                if (rightPort != null){
                    return rightPort.read();
                }
                break;
        }
        idle = false;
        return -1; // Invalid port index
    }

    public void writePort(String port, int value) {
        idle = true;
        switch (port){
            case ("UP"):
                if (upPort != null){
                    upPort.write(value);
                }
                break;
            case ("DOWN"):
                if (downPort != null){
                    downPort.write(value);
                }
                break;
            case ("LEFT"):
                if (leftPort != null){
                    leftPort.write(value);
                }
                break;
            case ("RIGHT"):
                if (rightPort != null){
                    rightPort.write(value);
                }
                break;
        }
        idle = false;
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

    public List<String> getProgram() {
        return program;
    }

    public void parseProgram(List<String> program) {
        for (String line : program) {
            instructions.add(new Instruction(line));
        }
    }

    private void executeNext() {
        ready = false;
        Instruction currMove = instructions.get(move);
        switch (currMove.command) {
            case "NOOP":
                // No operation
                break;
            case "MOVE":
                // Read [SRC] and write the result to [DST]
                accumulator = readPort(currMove.val_1);
                writePort(currMove.val_2, accumulator);
                break;
            case "SWAP":
                // Switch the value of the ACC register with the value of the BAK register
                int temp = accumulator;
                accumulator = backup;
                backup = temp;
                break;
            case "SAVE":
                // Write the value from the ACC register onto the BAK register
                backup = accumulator;
                break;
            case "ADD":
                // The value of [SRC] is added to the value in the ACC register
                accumulator += Integer.parseInt(currMove.val_1);
                break;
            case "SUB":
                // The value of [SRC] is subtracted from the value in the ACC register
                accumulator -= Integer.parseInt(currMove.val_1);
                break;
            case "NEGATE":
                // The value of the register ACC is negated
                accumulator -= accumulator;
                break;
            case "JUMP":
                // Jumps control of the program to the instruction following the given [LABEL]
                for (int i = 0; i < instructions.size(); i++) {
                    if (instructions.get(i).val_1.equals(currMove.val_1)){
                        move = i - 1;
                        break;
                    }
                }
                break;
            case "JEZ":
                // Jumps control of the program if the value in the register ACC is equal to zero
                if (accumulator == 0) {
                    for (int i = 0; i < instructions.size(); i++) {
                        if (instructions.get(i).val_1.equals(currMove.val_1)){
                            move = i - 1;
                            break;
                        }
                    }
                }
                break;
            case "JNZ":
                // Jumps control of the program if the value in the register ACC is not equal to zero
                if (accumulator != 0) {
                    for (int i = 0; i < instructions.size(); i++) {
                        if (instructions.get(i).val_1.equals(currMove.val_1)){
                            move = i - 1;
                            break;
                        }
                    }
                }
                break;
            case "JGZ":
                // Jumps control of the program if the value in the register ACC is greater than zero
                if (accumulator > 0) {
                    for (int i = 0; i < instructions.size(); i++) {
                        if (instructions.get(i).val_1.equals(currMove.val_1)){
                            move = i - 1;
                            break;
                        }
                    }
                }
                break;
            case "JLZ":
                // Jumps control of the program if the value in the register ACC is less than zero
                if (accumulator < 0) {
                    for (int i = 0; i < instructions.size(); i++) {
                        if (instructions.get(i).val_1.equals(currMove.val_1)){
                            move = i - 1;
                            break;
                        }
                    }
                }
                break;
            case "JRO":
                // Jumps control of the program to the instruction specified by the offset
                move += Integer.parseInt(currMove.val_1) - 1;
                break;
            case "SLEEP":
                // Sleeps for the given amount of time in seconds
                try {
                    Thread.sleep(Integer.parseInt(currMove.val_1) * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;
        }
    }


    public class Instruction{
        private String command;
        private String val_1;
        private String val_2;

        public Instruction(String line){
            val_1 = null;
            val_2 = null;
            String[] parts = line.split(" ");
            command = parts[0];
            if (parts.length > 1){
                val_1 = parts[1];
                if (parts.length > 2){
                    val_2 = parts[2];
                }
            }
        }

    }


}
