import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Silo silo;
    private ThreadManager threadManager;
    private AssemblyLanguage assemblyLanguage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize components
        silo = new Silo("", 4); // Assuming 4 ports
        threadManager = new ThreadManager();
        assemblyLanguage = new AssemblyLanguage(silo);

        // JavaFX UI setup
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter Assembly Instructions Here");
        VBox root = new VBox(textArea);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Virtual Machine Interface");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Example of running a program
        String program = "Sequence Counter\n" +
                "Sequences are zero-terminated\n" +
                "Read a sequence from INPUT.1\n" +
                "Write the sum to OUTPUT.1\n" +
                "Write the length to OUTPUT.2\n" +
                "GRID\n" +
                "3 2\n" +
                "MOVE UP ACC\n" +
                "MOVE ACC DOWN\n" +
                "MOVE ACC RIGHT\n" +
                "MOVE ACC DOWN\n" +
                "END\n" +
                "MOVE LEFT ACC\n" +
                "JEZ OUT\n" +
                "SWAP\n" +
                "ADD 1\n" +
                "JUMP DONE\n" +
                ":OUT:\n" +
                "SWAP\n" +
                "MOVE ACC DOWN\n" +
                "MOVE 0 ACC\n" +
                ":DONE:\n" +
                "SWAP\n" +
                "END\n" +
                "MOVE UP ACC\n" +
                "JEZ OUT\n" +
                "SWAP\n" +
                "ADD UP\n" +
                "JUMP DONE\n" +
                ":OUT:\n" +
                "SWAP\n" +
                "MOVE ACC DOWN\n" +
                "MOVE UP NIL\n" +
                "MOVE 0 ACC\n" +
                ":DONE:\n" +
                "SWAP\n" +
                "END\n" +
                "MOVE UP DOWN\n" +
                "END\n" +
                "MOVE UP DOWN\n" +
                "END\n" +
                "MOVE UP DOWN\n" +
                "END\n" +
                "INPUT\n" +
                "-1 0\n" +
                "35\n" +
                "0\n" +
                "62\n" +
                "51\n" +
                "81\n" +
                "54\n" +
                "12\n" +
                "0\n" +
                "51\n" +
                "63\n" +
                "50\n" +
                "67\n" +
                "48\n" +
                "0\n" +
                "49\n" +
                "23\n" +
                "26\n" +
                "0\n" +
                "33\n" +
                "79\n" +
                "76\n" +
                "0\n" +
                "0\n" +
                "94\n" +
                "0\n" +
                "79\n" +
                "0\n" +
                "98\n" +
                "15\n" +
                "0\n" +
                "53\n" +
                "35\n" +
                "45\n" +
                "12\n" +
                "79\n" +
                "0\n" +
                "19\n" +
                "71\n" +
                "0\n" +
                "END\n" +
                "OUTPUT.S\n" +
                "3 0\n" +
                "35\n" +
                "260\n" +
                "279\n" +
                "98\n" +
                "188\n" +
                "0\n" +
                "94\n" +
                "79\n" +
                "113\n" +
                "224\n" +
                "90\n" +
                "END\n" +
                "OUTPUT.L\n" +
                "3 1\n" +
                "1\n" +
                "5\n" +
                "5\n" +
                "3\n" +
                "3\n" +
                "0\n" +
                "1\n" +
                "1\n" +
                "2\n" +
                "5\n" +
                "2\n" +
                "END\n" +
                "END\n"; // Example program
        assemblyLanguage.executeProgram(program);
    }

    public static void main(String[] args) {
        launch(args);
    }

}