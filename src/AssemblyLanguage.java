import java.util.ArrayList;
import java.util.List;

public class AssemblyLanguage {

    private List<Instruction> instructions;
    private Silo silo;

    public AssemblyLanguage(Silo silo) {
        this.instructions = new ArrayList<>();
        this.silo = silo;
    }

    public void parseProgram(String program) {
        String[] splitInstructions = program.split("\\n");
        for (String line : splitInstructions) {
            instructions.add(new Instruction(line));
        }
    }

    public void executeInstructions() {
        for (Instruction cmd : instructions) {
            executeInstruction(cmd);
        }
    }

    private void executeInstruction(Instruction cmd) {
        String command = cmd.command;

        switch (command) {
            case "NOOP":
                //Do nothing
                break;
            case "MOVE":
                // MOVE [SRC] [DST]
                break;
            case "SWAP":
                //SWAP; Switch val from ACC reg with val from BAK reg
                break;
            case "SAVE":
                //SAVE; Write val from ACC reg onto BAK reg
                break;
            case "ADD":
                //ADD [SRC]; ACCreg += src
                //silo.setAccumulator(silo.getAccumulator() + value);
                break;
            case "SUB":
                //SUB [SRC]; ACCreg -= src
                break;
            case "NEGATE":
                //ACCreg = 0
                break;
            case "JUMP":
                //JUMP [LABEL]; jumps control to instruction following label
                break;
            case "JEZ":
                //JEZ [LABEL]; jumps if value in ACCreg = 0
                break;
            case "JNZ":
                //JNZ [LABEL]; jumps if ACCreg != 0
                break;
            case "JGZ":
                //JGZ [LABEL]; jumps if ACCreg > 0
                break;
            case "JLZ":
                //JLZ [LABEL]; jumps if ACCreg < 0
                break;
            case "JRO":
                //JRO [SRC]; jumps to instruction specified by offset; loop around if out of bounds
                //can be positive or negetive. JRO 0 = execute current step next, infinite loop
                break;
            case "SLEEP":
                //SLEEP [INT]; sleep for given seconds.
                break;

        }
    }

    // Getters
   //public List<String> getInstructions() {
        //return instructions;
    //}

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
