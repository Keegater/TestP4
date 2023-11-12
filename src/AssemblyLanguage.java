import java.util.HashMap;
import java.util.Map;

public class AssemblyLanguage {

    private Silo silo;
    private Map<String, Integer> labels;
    private int programCounter;

    public AssemblyLanguage(Silo silo) {
        this.silo = silo;
        this.labels = new HashMap<>();
        this.programCounter = 0;
    }

    public void executeProgram(String program) {
        String[] instructions = program.split("\n");
        // First pass to identify labels
        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i].matches(":.*:")) {
                labels.put(instructions[i].substring(1, instructions[i].length() - 1), i);
            }
        }
        // Execute instructions
        while (true) {
            if (programCounter >= instructions.length) {
                programCounter = 0; // Loop back to start
            }
            String instruction = instructions[programCounter];
            executeInstruction(instruction);
            programCounter++;
        }
    }

    private void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String command = parts[0];
        switch (command) {
            case "NOOP":
                // No operation
                break;
            case "MOVE":
                // Read [SRC] and write the result to [DST]
                int srcValue = getValue(parts[1]);
                setValue(parts[2], srcValue);
                break;
            case "SWAP":
                // Switch the value of the ACC register with the value of the BAK register
                int temp = silo.getAccumulator();
                silo.setAccumulator(silo.getBackup());
                silo.setBackup(temp);
                break;
            case "SAVE":
                // Write the value from the ACC register onto the BAK register
                silo.setBackup(silo.getAccumulator());
                break;
            case "ADD":
                // The value of [SRC] is added to the value in the ACC register
                silo.setAccumulator(silo.getAccumulator() + getValue(parts[1]));
                break;
            case "SUB":
                // The value of [SRC] is subtracted from the value in the ACC register
                silo.setAccumulator(silo.getAccumulator() - getValue(parts[1]));
                break;
            case "NEGATE":
                // The value of the register ACC is negated
                silo.setAccumulator(-silo.getAccumulator());
                break;
            case "JUMP":
                // Jumps control of the program to the instruction following the given [LABEL]
                programCounter = labels.get(parts[1]) - 1;
                break;
            case "JEZ":
                // Jumps control of the program if the value in the register ACC is equal to zero
                if (silo.getAccumulator() == 0) {
                    programCounter = labels.get(parts[1]) - 1;
                }
                break;
            case "JNZ":
                // Jumps control of the program if the value in the register ACC is not equal to zero
                if (silo.getAccumulator() != 0) {
                    programCounter = labels.get(parts[1]) - 1;
                }
                break;
            case "JGZ":
                // Jumps control of the program if the value in the register ACC is greater than zero
                if (silo.getAccumulator() > 0) {
                    programCounter = labels.get(parts[1]) - 1;
                }
                break;
            case "JLZ":
                // Jumps control of the program if the value in the register ACC is less than zero
                if (silo.getAccumulator() < 0) {
                    programCounter = labels.get(parts[1]) - 1;
                }
                break;
            case "JRO":
                // Jumps control of the program to the instruction specified by the offset
                programCounter += getValue(parts[1]) - 1;
                break;
            case "SLEEP":
                // Sleeps for the given amount of time in seconds
                try {
                    Thread.sleep(Integer.parseInt(parts[1]) * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;
            // Add more cases for other assembly instructions
        }
    }

    private int getValue(String src) {
        // Method to get value from source
        // Implement logic to get value from ACC, BAK, NIL, or ports
        return 0; // Placeholder
    }

    private void setValue(String dst, int value) {
        // Method to set value to destination
        // Implement logic to set value to ACC, BAK, NIL, or ports
    }

    // Getters and Setters
    public Silo getSilo() {
        return silo;
    }

    public void setSilo(Silo silo) {
        this.silo = silo;
    }

}